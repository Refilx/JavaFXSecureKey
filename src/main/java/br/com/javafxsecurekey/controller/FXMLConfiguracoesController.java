package br.com.javafxsecurekey.controller;

import br.com.javafxsecurekey.model.util.ToggleSwitch;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import javax.swing.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class FXMLConfiguracoesController implements Initializable {

    @FXML
    private Button btnSalvar;
    @FXML
    private ToggleSwitch tgConfirmCPF;

    private Map<String, Boolean> configutacao = new HashMap<>();

    @FXML
    void btnSalvarOnMouseClicked(MouseEvent event) {
        // Pergunto se a pessoa deseja mesmo salvar as configurações
        int opcao = JOptionPane.showOptionDialog(null, "Tem certeza que deseja salvar as configurações atuais?", "Confirmação final",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, 0);

        // Se o botão sim for apertado, Salvamos as configurações
        if (opcao == 0) {
            configutacao.put("switchConfirmaCPF", tgConfirmCPF.isSwitchedOn());
            FXMLNavigationPanelController.getSerializador().gravadorObjeto(configutacao);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configutacao = FXMLNavigationPanelController.getLoadConfig();
        tgConfirmCPF.setSwitchedOn(configutacao.get("switchConfirmaCPF"));
    }
}
