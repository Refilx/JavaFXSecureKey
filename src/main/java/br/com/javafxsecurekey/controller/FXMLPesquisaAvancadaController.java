package br.com.javafxsecurekey.controller;

import br.com.javafxsecurekey.model.dao.HistoricoDAO;
import br.com.javafxsecurekey.model.domain.Historico;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class FXMLPesquisaAvancadaController implements Initializable {

    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnPesquisar;
    @FXML
    private ListView<String> lvMeses;
    @FXML
    private TextField tfData;

    private ObservableList observableMapDadosPesquisados;
    private Map<String, String> mapMeses = new HashMap<>();
    private static Map<Integer, Historico> mapEmprestimos = new HashMap<>();
    private FilteredList<String> filteredMesesPesquisados;
    private String mesEscolhido;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mapMeses = HistoricoDAO.getMapMeses();

        observableMapDadosPesquisados = FXCollections.observableArrayList(mapMeses.keySet());

        filteredMesesPesquisados = new FilteredList<>(observableMapDadosPesquisados);

        lvMeses.setItems(filteredMesesPesquisados);

        // Listener para o filtro dinâmico conforme o usuário digita no TextField
        tfData.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredMesesPesquisados.setPredicate(item -> {
                if (newVal == null || newVal.isEmpty()) return true;
                String lower = newVal.toLowerCase();
                return item.toLowerCase().contains(lower);
            });
        });

        // Preenchendo o TextField ao clicar em um item da ListView
        lvMeses.setOnMouseClicked(e -> {
            String selected = lvMeses.getSelectionModel().getSelectedItem();

            //
            mesEscolhido = mapMeses.get(selected);

            if (selected != null) {
                tfData.setText(selected); // TextField
                lvMeses.setVisible(false); // ListView
            }
        });
    }

    @FXML
    void btnCancelarOnMouseClicked(MouseEvent event) {
        tfData.setText(null);
        Stage stageAtual = (Stage) ((Node) (btnCancelar)).getScene().getWindow();
        stageAtual.close();
    }

    @FXML
    void btnPesquisarOnMouseClicked(MouseEvent event) throws SQLException {
        lvMeses.setVisible(false);

        // Pesquisar a data específica no banco de dados e colocar em mapEmprestimos, depois chamar o prepararTabela() para carregar os dados
        FXMLEmprestimosFeitosController.setMapEmprestimos(HistoricoDAO.getMapMesPesquisado(mesEscolhido));

        tfData.setText(null);

        Stage stageAtual = (Stage) ((Node) (btnPesquisar)).getScene().getWindow();
        stageAtual.close();
    }

    @FXML
    void tfDataOnMouseClicked(MouseEvent event) {
        lvMeses.setVisible(true);
    }
}
