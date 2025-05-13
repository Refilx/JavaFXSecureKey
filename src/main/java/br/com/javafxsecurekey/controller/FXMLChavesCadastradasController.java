package br.com.javafxsecurekey.controller;

import br.com.javafxsecurekey.model.dao.ChaveDAO;
import br.com.javafxsecurekey.model.domain.Chave;
import br.com.javafxsecurekey.model.domain.Historico;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
    private TableColumn<Chave, String> tc_status;
    @FXML
    private TableColumn<?, ?> tc_temReserva;
    @FXML
    private TableView<Chave> tvChavesCadastradas;
    @FXML
    private TextField tfPesquisa;

    private ObservableList observableMap;
    private Map<Integer, Chave> mapEmprestimos = new HashMap<>();
    private FilteredList<Chave> filteredData;

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
        observableMap = FXCollections.observableArrayList(mapEmprestimos.values());

        // Criando a filtered list com os dados
        filteredData = new FilteredList<>(observableMap, p -> true);

        // Configurando a tabela após a pesquisa
        tvChavesCadastradas.setItems(filteredData);

        // Agora vamos colocar o listener no TextField
        tfPesquisa.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(chave -> {
                // Se o campo de filtro estiver vazio, exibe todos
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                // Adapte os campos conforme os atributos da sua classe Historico
                if (String.valueOf(chave.getNumeroChave()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (chave.getSala().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (chave.getBloco_predio().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (chave.getObservacoes().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (chave.getStatus().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(chave.getQuantChave()).contains(lowerCaseFilter)) {
                    return true;
                } else if (chave.getPossuiReserva().contains(lowerCaseFilter)) {
                    return true;
                }

                return false; // Não corresponde ao filtro
            });
        });

        /**
         * Código que adiciona cor ao status das chaves cadastradas, "INDISPONÍVEL" vermelho e "DISPONÍVEL" verde
         */
        tc_status.setCellFactory(column -> new TableCell<Chave, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle(""); // Resetar estilo quando a célula estiver vazia
                } else {
                    setText(item);
                    if ("INDISPONÍVEL".equals(item)) {
                        setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                    } else if ("DISPONÍVEL".equals(item)) {
                        setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                    } else {
                        setStyle(""); // Resetar estilo para outros valores
                    }
                }
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        prepararListaTabela();
    }


}
