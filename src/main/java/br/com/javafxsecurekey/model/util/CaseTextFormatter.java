package br.com.javafxsecurekey.model.util;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

/**
 * Essa Classe converte o texto digitado pelo usuário obrigando-o a ficar em Minúsculo ou Maiúsculo
 * @author Bruno Sousa da Silva
 */
public class CaseTextFormatter {

    /**
     * Força o texto digitado a ficar em Minúsculo
     * @param textField
     */
    public static void applyLowerCase(TextField textField) {
        if(textField != null) {
            UnaryOperator<TextFormatter.Change> filter = change -> {
                change.setText(change.getText().toLowerCase());
                return change;
            };

            TextFormatter<String> textFormatter = new TextFormatter<>(filter);
            textField.setTextFormatter(textFormatter);
        }
    }

    /**
     * Força o texto digitado a ficar em Maiúsculo
     * @param textField
     */
    public static void applyUpperCase(TextField textField) {
        if(textField != null) {
            UnaryOperator<TextFormatter.Change> filter = change -> {
                change.setText(change.getText().toUpperCase());
                return change;
            };

            TextFormatter<String> textFormatter = new TextFormatter<>(filter);
            textField.setTextFormatter(textFormatter);
        }
    }
}
