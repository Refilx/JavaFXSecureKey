package br.com.javafxsecurekey.controller;

import br.com.javafxsecurekey.model.dao.VerifyDAO;
import br.com.javafxsecurekey.model.domain.Pessoa;
import br.com.javafxsecurekey.model.util.TextFieldFormatter;
import br.com.javafxsecurekey.model.validator.CPFValidator;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.swing.*;

public class FXMLConfirmeComCPFController {
    @FXML
    private Button btnConfirmar;
    @FXML
    private TextField tfCPF;

    private static Pessoa pessoaEscolhida = null;
    private static boolean resultadoDaConfirmacao = false;

    public static boolean getResultadoDaConfirmacao() {
        return resultadoDaConfirmacao;
    }

    public static void setResultadoDefault() {
        resultadoDaConfirmacao = false;
    }

    public static void setPessoa(Pessoa pessoa) {
        pessoaEscolhida = pessoa;
    }

    public static void setPessoaDefault() {
        pessoaEscolhida = null;
    }

    @FXML
    void btnConfirmarOnMouseClicked(MouseEvent event) {
        resultadoDaConfirmacao = false;

        if(!tfCPF.getText().isEmpty() && pessoaEscolhida != null)
        {
            if(CPFValidator.validateCPF(tfCPF.getText()))
            {
                if(VerifyDAO.verifyCPFConfirmacaoParaEmprestimo(tfCPF.getText(), pessoaEscolhida.getIdPessoa()))
                {
                    resultadoDaConfirmacao = true;
                    JOptionPane.showMessageDialog(null, "CPF confirmado com sucesso!");
                    Stage stageAtual = (Stage) ((Node) (btnConfirmar)).getScene().getWindow();
                    stageAtual.close();
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "O CPF informado não está cadastrado!");
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null,"O CPF digitado é inválido\nPor favor, digite um CPF válido/correto!");
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null,"Preencha o campo com o CPF do solicitante para confirmar o empréstimo!");
        }
    }

    @FXML
    void tfCPFOnKeyReleased(KeyEvent event) {
        TextFieldFormatter tff = new TextFieldFormatter();
        tff.setMask("###.###.###-##");
        tff.setCaracteresValidos("0123456789");
        tff.setTf(tfCPF);
        tff.formatter();
    }

}
