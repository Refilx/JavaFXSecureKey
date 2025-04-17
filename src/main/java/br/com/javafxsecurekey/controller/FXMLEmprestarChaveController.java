package br.com.javafxsecurekey.controller;

import br.com.javafxsecurekey.model.dao.ChaveDAO;
import br.com.javafxsecurekey.model.dao.PessoaDAO;
import br.com.javafxsecurekey.model.domain.Chave;
import br.com.javafxsecurekey.model.domain.Pessoa;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class FXMLEmprestarChaveController implements Initializable {

    @FXML
    private Button btnCadastrar;

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

    private LinkedList<Pessoa> listPessoas = new LinkedList<>();
    private LinkedList<Chave> listChaves = new LinkedList<>();

    private ObservableList<String> obsPessoas;
    private ObservableList<String> obsChaves;

    @FXML
    void btnCadastrarMouseClicked(MouseEvent event) {
        scrollPaneSolicitante.setVisible(false);
        scrollPaneNumeroChave.setVisible(false);
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

        LinkedList<String> nomeCPF = new LinkedList<>();
        LinkedList<String> numChave = new LinkedList<>();

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
