package br.com.javafxsecurekey.controller;

import br.com.javafxsecurekey.model.dao.ChaveDAO;
import br.com.javafxsecurekey.model.dao.HistoricoDAO;
import br.com.javafxsecurekey.model.dao.PessoaDAO;
import br.com.javafxsecurekey.model.domain.Chave;
import br.com.javafxsecurekey.model.domain.Historico;
import br.com.javafxsecurekey.model.domain.Pessoa;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class FXMLEmprestarChaveController implements Initializable {

    @FXML
    private Button btnEmprestar;

    @FXML
    private Button btnCancelar;

    @FXML
    private Pane cadastroUserScreen;

    @FXML
    private ListView<String> lvNumChave;

    @FXML
    private ListView<String> lvSolicitante;

    @FXML
    private ScrollPane scrollPaneNumeroChave;

    @FXML
    private ScrollPane scrollPaneSolicitante;

    @FXML
    private TextArea taDescricao;

    @FXML
    private TextField tfNumChave;

    @FXML
    private TextField tfSolicitante;

    private Historico historico = new Historico();
    private Pessoa pessoaEscolhida = new Pessoa();
    private Chave chaveEscolhida = new Chave();

    private LinkedList<Pessoa> listPessoas = new LinkedList<>();
    private LinkedList<Chave> listChaves = new LinkedList<>();

    LinkedList<String> nomeCPF = new LinkedList<>();
    LinkedList<String> numChave = new LinkedList<>();

    private ObservableList<String> obsPessoas;
    private ObservableList<String> obsChaves;

    @FXML
    void btnEmprestarMouseClicked(MouseEvent event)
    {
        scrollPaneSolicitante.setVisible(false);
        scrollPaneNumeroChave.setVisible(false);

        if(!tfSolicitante.getText().isEmpty() && !tfNumChave.getText().isEmpty())
        {
            historico.setIdPessoa(pessoaEscolhida.getIdPessoa());
            historico.setIdChave(chaveEscolhida.getIdChave());
            historico.setNome(pessoaEscolhida.getNome());
            historico.setCargo(pessoaEscolhida.getCargo());
            historico.setNumeroChave(chaveEscolhida.getNumeroChave());
            historico.setObservacoes(taDescricao.getText());
            historico.setStatus("EM ABERTO");
            historico.setDataAbertura(new Timestamp(System.currentTimeMillis()));

            ChaveDAO.emprestarChave(historico);

            if(ChaveDAO.getResult())
            {
                tfSolicitante.setText(null);
                tfNumChave.setText(null);
                taDescricao.setText(null);
                scrollPaneNumeroChave.setVisible(false);
                scrollPaneSolicitante.setVisible(false);
            }
            ChaveDAO.setDefaultResult();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Selecione o solicitante e a chave emprestada, por favor!");
            alert.showAndWait();
        }
    }

    @FXML
    void btnCancelarMouseClicked(MouseEvent event) {
        scrollPaneSolicitante.setVisible(false);
        scrollPaneNumeroChave.setVisible(false);
    }

    @FXML
    void taDescricaoOnMouseClicked(MouseEvent event) {
        scrollPaneSolicitante.setVisible(false);
        scrollPaneNumeroChave.setVisible(false);
    }

    @FXML
    void tfNumChaveOnMouseClicked(MouseEvent event) {
        scrollPaneSolicitante.setVisible(false);
        scrollPaneNumeroChave.setVisible(true);
    }

    @FXML
    void tfSolicitanteOnMouseClicked(MouseEvent event) {
        scrollPaneSolicitante.setVisible(true);
        scrollPaneNumeroChave.setVisible(false);
    }

    private void carregarDados() {
        listPessoas = PessoaDAO.getPessoa();
        listChaves = ChaveDAO.getChave();

        for(Pessoa p : listPessoas)
            nomeCPF.add(p.getNome()+" "+p.getCPF());

        for(Chave c : listChaves)
            numChave.add(c.getNumeroChave()+" "+c.getSala());

        obsPessoas = FXCollections.observableArrayList(nomeCPF);
        obsChaves = FXCollections.observableArrayList(numChave);

        // FilteredLists para filtragem dinâmica
        FilteredList<String> filteredPessoas = new FilteredList<>(obsPessoas, p -> true);
        FilteredList<String> filteredChaves = new FilteredList<>(obsChaves, p -> true);

        // Associa as listas filtradas às ListViews
        lvSolicitante.setItems(filteredPessoas);
        lvNumChave.setItems(filteredChaves);

        // Listener para o filtro dinâmico conforme o usuário digita no TextField
        tfSolicitante.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredPessoas.setPredicate(item -> {
                if (newVal == null || newVal.isEmpty()) return true;
                String lower = newVal.toLowerCase();
                return item.toLowerCase().contains(lower);
            });
        });

        tfNumChave.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredChaves.setPredicate(item -> {
                if (newVal == null || newVal.isEmpty()) return true;
                String lower = newVal.toLowerCase();
                return item.toLowerCase().contains(lower);
            });
        });

        // Preenchendo o TextField ao clicar em um item da ListView
        lvSolicitante.setOnMouseClicked(e -> {
            String selected = lvSolicitante.getSelectionModel().getSelectedItem();

            for(Pessoa p : listPessoas)
            {
                if((p.getNome()+" "+p.getCPF()).equals(selected))
                {
                    pessoaEscolhida = p;
                    break;
                }
            }

            if (selected != null) {
                tfSolicitante.setText(selected);
            }
        });

        lvNumChave.setOnMouseClicked(e -> {
            String selected = lvNumChave.getSelectionModel().getSelectedItem();

            for(Chave c : listChaves)
            {
                if((c.getNumeroChave()+" "+c.getSala()).equals(tfNumChave.getText()))
                {
                    chaveEscolhida = c;
                    break;
                }
            }

            if (selected != null) {
                tfNumChave.setText(selected);
            }
        });

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        carregarDados();
    }
}
