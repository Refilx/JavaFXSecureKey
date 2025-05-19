package br.com.javafxsecurekey.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class FXMLPessoasCadastradasController {

    @FXML
    private TableColumn<?, ?> tc_CPF;

    @FXML
    private TableColumn<?, ?> tc_cargo;

    @FXML
    private TableColumn<?, ?> tc_email;

    @FXML
    private TableColumn<?, ?> tc_empresa;

    @FXML
    private TableColumn<?, ?> tc_nomePessoa;

    @FXML
    private TextField tfPesquisa;

    @FXML
    private TableView<?> tvPessoasCadastradas;

    @FXML
    void btnEditarDadosOnMouseClicked(MouseEvent event) {

    }

}
