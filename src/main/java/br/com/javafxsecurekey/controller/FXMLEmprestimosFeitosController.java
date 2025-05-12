package br.com.javafxsecurekey.controller;

import br.com.javafxsecurekey.model.dao.ChaveDAO;
import br.com.javafxsecurekey.model.dao.HistoricoDAO;
import br.com.javafxsecurekey.model.domain.Historico;
import br.com.javafxsecurekey.model.factory.ConnectionFactory;
import br.com.javafxsecurekey.model.util.TableViewToExcelPOI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class FXMLEmprestimosFeitosController implements Initializable {

    @FXML
    private MenuButton exportDataMenu;
    @FXML
    private Button btnExportToEXCEL;
    @FXML
    private Button btnExportToPDF;
    @FXML
    private Button btnDevolucao;
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
    private TableView<Historico> tvHistEmprestimo;
    @FXML
    private Button btnPesquisaAvancada;

    // Pesquisa Avançada Screen
//    @FXML
//    private Button btnCancelar;
//    @FXML
//    private Button btnPesquisar;
//    @FXML
//    private ListView<String> lvMeses;
//    @FXML
//    private TextField tfData;

    private static boolean carregarDados = true;

    private Historico dadosDoHistoricoEscolhido = new Historico();
    private ObservableList observableMap;
    //private ObservableList observableMapDadosPesquisados;
    private static Map<Integer, Historico> mapEmprestimos = new HashMap<>();
    //private Map<String, String> mapMeses = new HashMap<>();
    private FilteredList<Historico> filteredData;
    //private FilteredList<String> filteredMesesPesquisados;

    public static void setMapEmprestimos(Map<Integer, Historico> map) {
        mapEmprestimos = map;
    }

    void prepararListaTabela(Map<Integer, Historico> map) {
        tc_numChave.setCellValueFactory(new PropertyValueFactory<>("numeroChave"));
        tc_nomeSolicitante.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tc_cargo.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        tc_observacoes.setCellValueFactory(new PropertyValueFactory<>("observacoes"));
        tc_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        tc_dataAbertura.setCellValueFactory(new PropertyValueFactory<>("dataAberturaFormated"));
        tc_dataFechamento.setCellValueFactory(new PropertyValueFactory<>("dataFechamentoFormated"));

        // Pegando a lista de viagens do banco
        mapEmprestimos =  map;

        // configurando o observable list com os dados da lista do banco
        observableMap = FXCollections.observableArrayList(mapEmprestimos.values());

        // Criando a filtered list com os dados
        filteredData = new FilteredList<>(observableMap, p -> true);

        // Configurando a tabela após a pesquisa
        tvHistEmprestimo.setItems(filteredData);

        // Agora vamos colocar o listener no TextField
        tfPesquisa.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(historico -> {
                // Se o campo de filtro estiver vazio, exibe todos
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                // Adapte os campos conforme os atributos da sua classe Historico
                if (String.valueOf(historico.getNumeroChave()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (historico.getNome().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (historico.getCargo().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (historico.getObservacoes().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (historico.getStatus().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (historico.getDataAberturaFormated().contains(lowerCaseFilter)) {
                    return true;
                } else if (historico.getDataFechamento() != null && historico.getDataFechamentoFormated().contains(lowerCaseFilter)) {
                    return true;
                }

                return false; // Não corresponde ao filtro
            });
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mapEmprestimos = HistoricoDAO.getMapHistorico();

        prepararListaTabela(mapEmprestimos);
    }

    @FXML
    void tvEmprestimosFeitosOnMouseClicked(MouseEvent event) {
        btnDevolucao.setDisable(false);
    }

    @FXML
    void btnDevolucaoMouseClicked(MouseEvent event) {
        dadosDoHistoricoEscolhido = tvHistEmprestimo.getSelectionModel().getSelectedItem();

        int opcao = JOptionPane.showOptionDialog(null, "Deseja mesmo devolver essa chave?", "Confirmação",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[] {"Sim","Não"}, null);

        //Testa se o campo dataFechamento está vazio e a opcao foi Sim
        if(dadosDoHistoricoEscolhido.getDataFechamento() == null && opcao == 0) {
            //
            ChaveDAO.devolverChave(dadosDoHistoricoEscolhido);

            mapEmprestimos = HistoricoDAO.getMapHistorico();

            prepararListaTabela(mapEmprestimos);

        } else if(dadosDoHistoricoEscolhido.getDataFechamento() != null && opcao == 0) {
            JOptionPane.showMessageDialog(null, "A chave já foi devolvida");
        }
    }

    @FXML
    void exportToEXCELOnMouseClicked(MouseEvent event) {
        TableViewToExcelPOI<Historico> exporter = new TableViewToExcelPOI<>();
        exporter.exportToExcel(tvHistEmprestimo, btnExportToEXCEL.getScene().getWindow());
    }

    @FXML
    void exportToPDFOnMouseClicked(MouseEvent event) throws Exception {

        // Caminho do arquivo de imagem dentro do classpath
        InputStream logoStream = getClass().getResourceAsStream("/br/com/passabus/view/imgs/logo.png");
        InputStream mainStream = getClass().getResourceAsStream("/br/com/passabus/view/imgs/mainbus.png");

        if (logoStream == null) {
            throw new RuntimeException("Imagem não encontrada: logo.png");
        }

        // Criamos um mapa de parâmetros para passar ao relatório
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("logo", logoStream); // Passa a imagem como parâmetro
        parameters.put("mainbus", mainStream);

        // Carregamos o relatório por meio de um InputStream
        InputStream jasperStream = getClass().getResourceAsStream("/br/com/passabus/relatorios/JAVAFXFXMLRelatórioPassagem.jasper");

        if (jasperStream == null) {
            throw new JRException("Arquivo do relatório não encontrado!");
        }


        // O objeto JasperReport serve para carregar o arquivo do relatório.jarper
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);

        // JasperPrint irá buscar os valores que serão passados para a impressão no banco de dados
        // null: caso não existam filtros
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, ConnectionFactory.createConnectionToMySQL());

        // JasperViewer serve para poder exibir o relatório e carregá-lo em uma página visualizável
        // false: serve para não deixar fechar a aplicação principal
        JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
        jasperViewer.setVisible(true);

    }

    @FXML
    void btnPesquisaAvancadaOnMouseClicked(MouseEvent event) throws IOException {
        // Carrega o FXML do pop-up
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/javafxsecurekey/view/screens/FXMLPesquisaAvancadaScreen.fxml"));
        Parent popupRoot = loader.load();

        // Cria um novo Stage para o pop-up
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);  // Modalidade que bloqueia todas as janelas ate a pop up ser fechada
        popupStage.setResizable(false);  // Impede redimensionamento

        // Define a cena e exibe
        Scene popupScene = new Scene(popupRoot);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();  // Aguarda até o pop-up ser fechado

        prepararListaTabela(mapEmprestimos);
    }

}
