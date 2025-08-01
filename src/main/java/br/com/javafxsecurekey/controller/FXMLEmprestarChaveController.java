package br.com.javafxsecurekey.controller;

import br.com.javafxsecurekey.model.dao.ChaveDAO;
import br.com.javafxsecurekey.model.dao.PessoaDAO;
import br.com.javafxsecurekey.model.dao.VerifyDAO;
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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
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
    private TextArea taDescricao;
    @FXML
    private TextField tfNumChave;
    @FXML
    private TextField tfSolicitante;

    private Historico historico = new Historico();
    private Pessoa pessoaEscolhida = new Pessoa();
    private Chave chaveEscolhida = new Chave();

    private Map<Integer, Pessoa> mapPessoas = new HashMap<>();
    private Map<Integer, Chave> mapChaves = new HashMap<>();

    private Map<String, Integer> mapLvPessoaValues = new HashMap<>();
    private Map<String, Integer> mapLvChaveValues = new HashMap<>();

    private ObservableList<String> obsPessoas;
    private ObservableList<String> obsChaves;

    @FXML
    void btnEmprestarMouseClicked(MouseEvent event)
    {
        lvSolicitante.setVisible(false);
        lvNumChave.setVisible(false);

        // Verifica se a pessoa já está tentando pegar a mesma chave
        if(VerifyDAO.verifyAlreadyBorrowed(chaveEscolhida.getIdChave(), pessoaEscolhida.getIdPessoa()))
        {

            // Verifica se os campos de texto está preechido e se o usuário escolheu uma pessoa e um chave
            if(!tfSolicitante.getText().isEmpty() && !tfNumChave.getText().isEmpty()
                    && pessoaEscolhida != null && chaveEscolhida != null)
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
                    lvNumChave.setVisible(false);
                    lvSolicitante.setVisible(false);
                    pessoaEscolhida = null;
                    chaveEscolhida = null;
                }
                ChaveDAO.setDefaultResult();
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Selecione o solicitante e a chave emprestada, por favor!");
                alert.showAndWait();
            }
        }
    }

    @FXML
    void btnCancelarMouseClicked(MouseEvent event) {
        lvSolicitante.setVisible(false);
        lvNumChave.setVisible(false);
        tfSolicitante.setText(null);
        tfNumChave.setText(null);
        taDescricao.setText(null);
        pessoaEscolhida = null;
        chaveEscolhida = null;
    }

    @FXML
    void taDescricaoOnMouseClicked(MouseEvent event) {
        lvSolicitante.setVisible(false);
        lvNumChave.setVisible(false);
    }

    @FXML
    void tfNumChaveOnMouseClicked(MouseEvent event) {
        lvSolicitante.setVisible(false);
        lvNumChave.setVisible(true);
    }

    @FXML
    void tfSolicitanteOnMouseClicked(MouseEvent event) {
        lvSolicitante.setVisible(true);
        lvNumChave.setVisible(false);
    }

    private void carregarDados() {
        // Listas principais recebem os dados do banco
        mapPessoas = PessoaDAO.getMapPessoaAtiva();
        mapChaves = ChaveDAO.getMapChave();

        // Passando alguns dados específicos para listas auxiliares que exibirão os dados na ListView
        for(Map.Entry<Integer, Pessoa> mapEntry : mapPessoas.entrySet())
            mapLvPessoaValues.putIfAbsent(mapEntry.getValue().getNome()+" - "+mapEntry.getValue().getCPF(), mapEntry.getKey()); // formatando a exibição dos dados: Nome da pessoa + CPF: 000.***.***-00

        for(Map.Entry<Integer, Chave> mapEntry : mapChaves.entrySet())
            mapLvChaveValues.putIfAbsent("Chave: "+mapEntry.getValue().getNumeroChave()+" - Sala: "+mapEntry.getValue().getSala(), mapEntry.getKey());

        // Carregamos as listas auxiliares com os dados exibíveis
        obsPessoas = FXCollections.observableArrayList(mapLvPessoaValues.keySet());
        obsChaves = FXCollections.observableArrayList(mapLvChaveValues.keySet());

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

        // Listener para o filtro dinâmico conforme o usuário digita no TextField
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

            // Os dados da pessoa Escolhida está na mapPessoas, mas a chave da mapPessoas é o valor que está na mapLvPessoaValues e o selected (valor selecionado) é a chave da mapLvPessoaValues
            pessoaEscolhida = mapPessoas.get(mapLvPessoaValues.get(selected));

            if (selected != null) {
                tfSolicitante.setText(selected); // TextField
                lvSolicitante.setVisible(false); // ListView
            }
        });

        // Preenchendo o TextField ao clicar em um item da ListView
        lvNumChave.setOnMouseClicked(e -> {
            String selected = lvNumChave.getSelectionModel().getSelectedItem();

            // Os dados da chave Escolhida está na mapChaves, mas a chave da mapChaves é o valor que está na mapLvChaveValues e o selected (valor selecionado) é a chave da mapLvChaveValues
            chaveEscolhida = mapChaves.get(mapLvChaveValues.get(selected));

            if (selected != null) {
                tfNumChave.setText(selected); // TextField
                lvNumChave.setVisible(false); // ListView
            }
        });

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        carregarDados();
    }
}
