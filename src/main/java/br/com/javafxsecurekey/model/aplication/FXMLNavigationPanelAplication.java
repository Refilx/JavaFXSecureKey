/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package br.com.javafxsecurekey.model.aplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 *
 * @author revil
 */
public class FXMLNavigationPanelAplication extends Application {
    
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/br/com/javafxsecurekey/view/screens/FXMLNavigationPanel.fxml"));
        Parent root = fxmlLoader.load();
        
        Scene scene = new Scene(root);
//        scene.getStylesheets().add(getClass().getResource("/br/com/javafxsecurekey/view/styles/dropdownbuttons.css").toExternalForm());

        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
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
