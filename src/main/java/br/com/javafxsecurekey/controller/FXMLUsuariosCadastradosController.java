package br.com.javafxsecurekey.controller;

import br.com.javafxsecurekey.model.dao.UsuarioDAO;
import br.com.javafxsecurekey.model.domain.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
    private TableView<Usuario> tvUsuariosCadastrados;

    private ObservableList observableMap;
    private Map<Integer, Usuario> mapUsuarios = new HashMap<>();
    private FilteredList<Usuario> filteredData;

    void prepararListaTabela() {
        tc_username.setCellValueFactory(new PropertyValueFactory<>("username"));
        tc_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        tc_cargo.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        tc_empresa.setCellValueFactory(new PropertyValueFactory<>("empresa"));
        tc_role.setCellValueFactory(new PropertyValueFactory<>("role"));

        // Pegando a lista de viagens do banco
        mapUsuarios = UsuarioDAO.getMapUsuario();

        // configurando o observable list com os dados da lista do banco
        observableMap = FXCollections.observableArrayList(mapUsuarios.values());

        // Criando a filtered list com os dados
        filteredData = new FilteredList<>(observableMap, p -> true);

        // Configurando a tabela após a pesquisa
        tvUsuariosCadastrados.setItems(filteredData);

        // Agora vamos colocar o listener no TextField
        tfPesquisa.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(usuario -> {
                // Se o campo de filtro estiver vazio, exibe todos
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                // Adapte os campos conforme os atributos da sua classe Historico
                if (usuario.getUsername().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (usuario.getEmail().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (usuario.getCargo().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (usuario.getEmpresa().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (usuario.getRole().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                return false; // Não corresponde ao filtro
            });
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        prepararListaTabela();
    }

}
