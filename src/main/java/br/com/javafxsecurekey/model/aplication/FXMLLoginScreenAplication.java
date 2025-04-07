package br.com.javafxsecurekey.model.aplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * Classe que Executa a tela de login do sistema
 * @author Bruno Sousa
 */
public class FXMLLoginScreenAplication extends Application {
    
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/br/com/javafxsecurekey/view/screens/FXMLLoginScreen.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Tela de Login Secure Key (JavaFX)");
        stage.setResizable(false);
        stage.show();
        
    }

    public void trocarDeTela(Stage stageAtual, String novaTela) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(novaTela));
        Scene novaCena = new Scene(loader.load());

        stageAtual.close();

        Stage novoStage = new Stage();
        novoStage.setScene(novaCena);
        novoStage.centerOnScreen();
        novoStage.initStyle(StageStyle.UNDECORATED);
        novoStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
