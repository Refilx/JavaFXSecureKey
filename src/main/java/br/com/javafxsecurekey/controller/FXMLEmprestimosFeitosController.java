package br.com.javafxsecurekey.controller;

import br.com.javafxsecurekey.model.dao.HistoricoDAO;
import br.com.javafxsecurekey.model.domain.Historico;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class FXMLEmprestimosFeitosController implements Initializable {

    @FXML
    private MenuButton GPMenu;
    @FXML
    private Button btnCadastrar;
    @FXML
    private TableColumn<?, ?> tc_cargo;
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

    private ObservableList observableList;
    private Map<Integer, Historico> mapEmprestimos = new HashMap<>();

    void prepararListaTabela() {
        tc_numChave.setCellValueFactory(new PropertyValueFactory<>("numeroChave"));
        tc_nomeSolicitante.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tc_cargo.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        tc_observacoes.setCellValueFactory(new PropertyValueFactory<>("observacoes"));
        tc_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        tc_dataAbertura.setCellValueFactory(new PropertyValueFactory<>("dataAbertura"));
        tc_dataFechamento.setCellValueFactory(new PropertyValueFactory<>("dataFechamento"));

        // Pegando a lista de viagens do banco
        mapEmprestimos =  HistoricoDAO.getMapHistorico();

        // configurando o observable list com os dados da lista do banco
        observableList = FXCollections.observableArrayList(mapEmprestimos.values());

        // Configurando a tabela após a pesquisa
        tvHistEmprestimo.setItems(observableList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        prepararListaTabela();
    }

    @FXML
    void btnDevolucaoMouseClicked(MouseEvent event) {

    }

    @FXML
    void exportToEXCEL(MouseEvent event) {

    }

    @FXML
    void exportToPDF(MouseEvent event) {

    }

}
