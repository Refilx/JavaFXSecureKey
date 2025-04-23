package br.com.javafxsecurekey.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class FXMLUsuariosCadastradosController {

    @FXML
    private TableColumn<?, ?> tc_cargo;

    @FXML
    private TableColumn<?, ?> tc_email;

    @FXML
    private TableColumn<?, ?> tc_empresa;

    @FXML
    private TableColumn<?, ?> tc_role;

    @FXML
    private TableColumn<?, ?> tc_username;

    @FXML
    private TextField tfPesquisa;

    @FXML
    private TableView<?> tvUsuariosCadastrados;

}
