package br.com.javafxsecurekey.model.aplication;

import br.com.javafxsecurekey.model.dao.LogDAO;
import br.com.javafxsecurekey.model.domain.Log;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * @author Bruno Sousa da Silva
 */
public class FXMLNavigationPanelAplication extends Application {

    private LogDAO logDoUsuario = new LogDAO();
    private Log usuarioLogado;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/br/com/javafxsecurekey/view/screens/FXMLNavigationPanel.fxml"));
        Parent root = fxmlLoader.load();
        
        Scene scene = new Scene(root);
//        scene.getStylesheets().add(getClass().getResource("/br/com/javafxsecurekey/view/styles/dropdownbuttons.css").toExternalForm());

        stage.setScene(scene);
        stage.initStyle(StageStyle.DECORATED);
        usuarioLogado = logDoUsuario.getUltimoLogado();
        stage.setOnCloseRequest(event -> {

            int opcao = JOptionPane.showOptionDialog(null, "Tem certeza que deseja sair?", "Confirmação",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[] {"Sim", "Não"}, null);

            //Se o usuário selecionar a opção "Sim", a aplicação irá fechar a tela de dashboard e voltar para a tela de login
            if(opcao == 0){
                LogDAO logDAO = new LogDAO();
                logDAO.saveLogout(usuarioLogado);

                stage.close(); // fecha a tela de dashboard

                // Chama a tela de login e reinicia a aplicação
                FXMLLoginScreenAplication login = new FXMLLoginScreenAplication();
                try {
                    login.start(new Stage());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                event.consume();
            }
        });
        stage.show();

    }

//    public void trocarDeTela(Stage stageAtual, String novaTela) throws IOException {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource(novaTela));
//        Scene novaCena = new Scene(loader.load());
//        stageAtual.setScene(novaCena);
//    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
