package br.com.javafxsecurekey.controller;

import br.com.javafxsecurekey.model.dao.PessoaDAO;
import br.com.javafxsecurekey.model.dao.UsuarioDAO;
import br.com.javafxsecurekey.model.domain.Pessoa;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class FXMLPessoasCadastradasController implements Initializable {

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
    private TableView<Pessoa> tvPessoasCadastradas;


    private ObservableList observableMap;
    private Map<Integer, Pessoa> mapPessoas = new HashMap<>();
    private FilteredList<Pessoa> filteredData;

    void prepararListaTabela() {
        tc_nomePessoa.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tc_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        tc_cargo.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        tc_empresa.setCellValueFactory(new PropertyValueFactory<>("empresa"));
        tc_CPF.setCellValueFactory(new PropertyValueFactory<>("CPF"));

        // Pegando a lista de viagens do banco
        mapPessoas = PessoaDAO.getMapPessoa();

        // configurando o observable list com os dados da lista do banco
        observableMap = FXCollections.observableArrayList(mapPessoas.values());

        // Criando a filtered list com os dados
        filteredData = new FilteredList<>(observableMap, p -> true);

        // Configurando a tabela após a pesquisa
        tvPessoasCadastradas.setItems(filteredData);

        // Agora vamos colocar o listener no TextField
        tfPesquisa.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(pessoa -> {
                // Se o campo de filtro estiver vazio, exibe todos
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                // Adapte os campos conforme os atributos da sua classe Historico
                if (pessoa.getNome().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (pessoa.getEmail().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (pessoa.getCargo().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (pessoa.getEmpresa().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (pessoa.getCPF().toLowerCase().contains(lowerCaseFilter)) {
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


    @FXML
    void btnEditarDadosOnMouseClicked(MouseEvent event) {

    }

}
