package br.com.javafxsecurekey.controller;

import br.com.javafxsecurekey.model.dao.UsuarioDAO;
import br.com.javafxsecurekey.model.domain.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class FXMLUsuariosCadastradosController implements Initializable {

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

    private ObservableList observableList;
    private Map<Integer, Usuario> mapUsuarios = new HashMap<>();

    void prepararListaTabela() {
        tc_username.setCellValueFactory(new PropertyValueFactory<>("username"));
        tc_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        tc_cargo.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        tc_empresa.setCellValueFactory(new PropertyValueFactory<>("empresa"));
        tc_role.setCellValueFactory(new PropertyValueFactory<>("role"));

        // Pegando a lista de viagens do banco
        mapUsuarios = UsuarioDAO.getMapUsuario();

        // configurando o observable list com os dados da lista do banco
        observableList = FXCollections.observableArrayList(mapUsuarios.values());

        // Configurando a tabela após a pesquisa
        tvUsuariosCadastrados.setItems(observableList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        prepararListaTabela();
    }

}
