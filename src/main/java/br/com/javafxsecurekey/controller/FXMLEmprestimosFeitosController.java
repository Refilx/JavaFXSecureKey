package br.com.javafxsecurekey.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class FXMLEmprestimosFeitosController {

    @FXML
    private MenuButton GPMenu;

    @FXML
    private Button btnCadastrar;

    @FXML
    private TableColumn<?, ?> tc_dataAbertura;

    @FXML
    private TableColumn<?, ?> tc_dataFechamento;

    @FXML
    private TableColumn<?, ?> tc_nomeSolicitante;

    @FXML
    private TableColumn<?, ?> tc_numChave;

    @FXML
    private TableColumn<?, ?> tc_observacoes;

    @FXML
    private TableColumn<?, ?> tc_status;

    @FXML
    private TextField tfPesquisa;

    @FXML
    private TableView<?> tvHistEmprestimo;

    @FXML
    void btnCadastrarMouseClicked(MouseEvent event) {

    }

    @FXML
    void exportToEXCEL(MouseEvent event) {

    }

    @FXML
    void exportToPDF(MouseEvent event) {

    }

}
