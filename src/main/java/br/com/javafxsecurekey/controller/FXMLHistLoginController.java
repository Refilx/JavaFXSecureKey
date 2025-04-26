package br.com.javafxsecurekey.controller;

import br.com.javafxsecurekey.model.dao.LogDAO;
import br.com.javafxsecurekey.model.domain.Log;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class FXMLHistLoginController implements Initializable {

    @FXML
    private TableColumn<?, ?> tc_login;

    @FXML
    private TableColumn<?, ?> tc_logout;

    @FXML
    private TableColumn<?, ?> tc_role;

    @FXML
    private TableColumn<?, ?> tc_username;

    @FXML
    private TableView<?> tvHistLogin;

    private ObservableList observableList;
    private Map<Integer, Log> mapLogs = new HashMap<>();

    void prepararListaTabela() {
        tc_login.setCellValueFactory(new PropertyValueFactory<>("dtLogin"));
        tc_logout.setCellValueFactory(new PropertyValueFactory<>("dtLogout"));
        tc_role.setCellValueFactory(new PropertyValueFactory<>("role"));
        tc_username.setCellValueFactory(new PropertyValueFactory<>("username"));

        // Pegando a lista de viagens do banco
        mapLogs =  LogDAO.getMapLogs();

        // configurando o observable list com os dados da lista do banco
        observableList = FXCollections.observableArrayList(mapLogs.values());

        // Configurando a tabela após a pesquisa
        tvHistLogin.setItems(observableList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        prepararListaTabela();
    }
}
