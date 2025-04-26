package br.com.javafxsecurekey.controller;

import br.com.javafxsecurekey.model.dao.ChaveDAO;
import br.com.javafxsecurekey.model.domain.Chave;
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

public class FXMLChavesCadastradasController implements Initializable {

    @FXML
    private TableColumn<?, ?> tc_bloco_predio;
    @FXML
    private TableColumn<?, ?> tc_nomeSala;
    @FXML
    private TableColumn<?, ?> tc_numChave;
    @FXML
    private TableColumn<?, ?> tc_observacoes;
    @FXML
    private TableColumn<?, ?> tc_quantidade;
    @FXML
    private TableColumn<?, ?> tc_status;
    @FXML
    private TableColumn<?, ?> tc_temReserva;
    @FXML
    private TableView<?> tvChavesCadastradas;


    private ObservableList observableList;
    private Map<Integer, Chave> mapEmprestimos = new HashMap<>();

    void prepararListaTabela() {
        tc_numChave.setCellValueFactory(new PropertyValueFactory<>("numeroChave"));
        tc_nomeSala.setCellValueFactory(new PropertyValueFactory<>("sala"));
        tc_bloco_predio.setCellValueFactory(new PropertyValueFactory<>("bloco_predio"));
        tc_observacoes.setCellValueFactory(new PropertyValueFactory<>("observacoes"));
        tc_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        tc_quantidade.setCellValueFactory(new PropertyValueFactory<>("quantChave"));
        tc_temReserva.setCellValueFactory(new PropertyValueFactory<>("possuiReserva"));

        // Pegando a lista de viagens do banco
        mapEmprestimos =  ChaveDAO.getMapChave();

        // configurando o observable list com os dados da lista do banco
        observableList = FXCollections.observableArrayList(mapEmprestimos.values());

        // Configurando a tabela após a pesquisa
        tvChavesCadastradas.setItems(observableList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        prepararListaTabela();
    }


}
