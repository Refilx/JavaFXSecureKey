package br.com.javafxsecurekey.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class FXMLCadastroChaveController {

    @FXML
    private ToggleGroup PossuiChaveReserva;

    @FXML
    private Button btnCadastrar;

    @FXML
    private Button btnCancelar;

    @FXML
    private Pane cadastroUserScreen;

    @FXML
    private RadioButton rb_nao;

    @FXML
    private RadioButton rb_sim;

    @FXML
    private TextArea taDescricao;

    @FXML
    private TextField tfBloco;

    @FXML
    private TextField tfNomeSala;

    @FXML
    private TextField tfNumeroChave;

    @FXML
    void btnCadastrarMouseClicked(MouseEvent event) {

    }

    @FXML
    void btnCancelarMouseClicked(MouseEvent event) {

    }

}
