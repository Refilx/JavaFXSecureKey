package br.com.javafxsecurekey.controller;

import br.com.javafxsecurekey.model.dao.ChaveDAO;
import br.com.javafxsecurekey.model.dao.HistoricoDAO;
import br.com.javafxsecurekey.model.dao.PessoaDAO;
import br.com.javafxsecurekey.model.domain.Chave;
import br.com.javafxsecurekey.model.domain.Historico;
import br.com.javafxsecurekey.model.domain.Pessoa;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
            boolean pessoaNoBanco = false;
            Pessoa pessoaEscolhida = null;
            Chave chaveEscolhida = null;

            for(Pessoa p : listPessoas)
            {
                if((p.getNome()+" "+p.getCPF()).equals(tfSolicitante.getText()))
                {
                    pessoaNoBanco = true;
                    pessoaEscolhida = p;
                    break;
                }
            }

            if(pessoaNoBanco)
            {
                boolean chaveNoBanco = false;

                for(Chave c : listChaves)
                {
                    if((c.getNumeroChave()+" "+c.getSala()).equals(tfNumChave.getText()))
                    {
                        chaveNoBanco = true;
                        chaveEscolhida = c;
                        break;
                    }
                }

                if(chaveNoBanco)
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
                    Alert alert = new Alert(Alert.AlertType.WARNING, "A chave digitada para empréstimo não está cadastrada no sistema!");
                    alert.showAndWait();
                }
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.WARNING, "A pessoa digitada como solicitante não está cadastrada no sistema!");
                alert.showAndWait();
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Preencha o campo de solicitante e da chave emprestada, por favor!");
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

        lvSolicitante.setItems(obsPessoas);
        lvNumChave.setItems(obsChaves);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        carregarDados();
    }
}
