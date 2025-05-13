package br.com.javafxsecurekey.controller;

import br.com.javafxsecurekey.model.aplication.FXMLLoginScreenAplication;
import br.com.javafxsecurekey.model.dao.ChaveDAO;
import br.com.javafxsecurekey.model.dao.LogDAO;
import br.com.javafxsecurekey.model.domain.Chave;
import br.com.javafxsecurekey.model.domain.Log;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FXMLNavigationPanelController implements Initializable {

    @FXML
    private MenuButton GCMenu;
    @FXML
    private MenuButton GPMenu;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Button btnDashboard;
    @FXML
    private Button btnSair;
    @FXML
    private AnchorPane dashboardScreen;
    @FXML
    private Circle imgCircle;
    @FXML
    private Label labelRole;
    @FXML
    private Label labelUsername;
    @FXML
    private VBox vbox;
    @FXML
    private TextField tfChaveRequisitadaMes;
    @FXML
    private TextField tfSalaMaisAcessada;

    private LogDAO logDoUsuario = new LogDAO();
    private Log usuarioLogado;

    @FXML
    void getCadastroChaveScreen(MouseEvent event) throws IOException {
        loadPage("FXMLCadastroChaveScreen");
        GCMenu.hide();
    }

    @FXML
    void getCadastroPessoaScreen(MouseEvent event) throws IOException {
        loadPage("FXMLCadastroPessoaScreen");
        GPMenu.hide();
    }

    @FXML
    private void getCadastroUserScreen(MouseEvent event) throws IOException {
        loadPage("FXMLCadastroUserScreen");
        GPMenu.hide();
    }

    @FXML
    void getChavesCadastradasScreen(MouseEvent event) throws IOException {
        loadPage("FXMLChavesCadastradasScreen");
        GCMenu.hide();
    }

    @FXML
    private void getDashboardScreen(MouseEvent event) {
        borderPane.setCenter(dashboardScreen);
    }

    @FXML
    void getEmprestarChaveScreen(MouseEvent event) throws IOException {
        loadPage("FXMLEmprestarChaveScreen");
    }

    @FXML
    void getEmprestimosFeitosScreen(MouseEvent event) throws IOException {
        loadPage("FXMLEmprestimosFeitosScreen");
        GCMenu.hide();
    }

    @FXML
    private void getHistLoginScreen(MouseEvent event) throws IOException {
        loadPage("FXMLHistLoginScreen");
        GPMenu.hide();
    }

    @FXML
    void getUsuariosCadastradosScreen(MouseEvent event) throws IOException {
        loadPage("FXMLUsuariosCadastradosScreen");
        GPMenu.hide();
    }

    @FXML
    private void logout(MouseEvent event) throws IOException {
        // Forma de adicionar icone ( new ImageIcon(getClass().getResource("/br/com/javafxsecurekey/view/imgs/logout-door.png"), "logout") )
        // Adicionar o código acima no espaço para icon, OBS a imagem deve ser pequena para ficar arrumado no JOptionPane
        int opcao = JOptionPane.showOptionDialog(null, "Tem certeza que deseja sair?", "Confirmação",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[] {"Sim", "Não"}, null);

        //Se o usuário selecionar a opção "Sim", a aplicação irá fechar a tela de dashboard e voltar para a tela de login
        if(opcao == 0){
            LogDAO logDAO = new LogDAO();
            logDAO.saveLogout(usuarioLogado);

            Stage stageAtual = (Stage) ((Node) btnSair).getScene().getWindow();
            stageAtual.close(); // fecha a tela de dashboard

            // Chama a tela de login e reinicia a aplicação
            FXMLLoginScreenAplication login = new FXMLLoginScreenAplication();
            login.start(new Stage());
        }
    }
    
    public void loadPage(String page) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/br/com/javafxsecurekey/view/screens/"+page+".fxml"));
        Parent root = fxmlLoader.load();
        
        borderPane.setCenter(root);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usuarioLogado = logDoUsuario.getUltimoLogado();

        labelRole.setText(usuarioLogado.getRole());
        labelUsername.setText(usuarioLogado.getUsername());

        Chave c = ChaveDAO.getChaveSalaPopular();

        tfChaveRequisitadaMes.setText(""+c.getNumeroChave());
        tfSalaMaisAcessada.setText(c.getSala());

//            Image img = new Image(getClass().getResource("/br/com/javafxsecurekey/view/imgs/").toExternalForm(), false);
//            imgCircle.setFill(new ImagePattern(img));

    }
    
}
