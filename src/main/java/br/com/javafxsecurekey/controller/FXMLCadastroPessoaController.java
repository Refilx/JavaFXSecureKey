package br.com.javafxsecurekey.controller;

import br.com.javafxsecurekey.model.dao.PessoaDAO;
import br.com.javafxsecurekey.model.dao.VerifyDAO;
import br.com.javafxsecurekey.model.domain.Pessoa;
import br.com.javafxsecurekey.model.util.TextFieldFormatter;
import br.com.javafxsecurekey.model.validator.CPFValidator;
import br.com.javafxsecurekey.model.validator.EmailValidator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import javax.swing.*;
import java.time.LocalDate;

public class FXMLCadastroPessoaController {

    @FXML
    private Button btnCadastrar;

    @FXML
    private Button btnCancelar;

    @FXML
    private Pane cadastroUserScreen;

    @FXML
    private TextField tfCEP;

    @FXML
    private TextField tfCPF;

    @FXML
    private TextField tfCargo;

    @FXML
    private TextField tfCidade;

    @FXML
    private TextField tfEmail;

    @FXML
    private TextField tfEmpresa;

    @FXML
    private TextField tfEndereco;

    @FXML
    private TextField tfNome;

    @FXML
    private TextField tfTelefone;

    private Pessoa pessoa = new Pessoa();

    @FXML
    void btnCadastrarMouseClicked(MouseEvent event) {
        // Verifico se todos os campos estão preenchidos
        if(
                !tfNome.getText().isEmpty() &&
                !tfEmail.getText().isEmpty() &&
                !tfEmpresa.getText().isEmpty() &&
                !tfCargo.getText().isEmpty() &&
                !tfEndereco.getText().isEmpty() &&
                !tfCidade.getText().isEmpty() &&
                !tfCEP.getText().isEmpty() &&
                !tfCPF.getText().isEmpty() &&
                !tfTelefone.getText().isEmpty()
        ) {
            // Valida o e-mail digitado para saber se é um e-mail válido
            if(EmailValidator.isValidEmail(tfEmail.getText())) {

                // Verificar se o email já foi cadastrado no banco de dados
                if(!VerifyDAO.verifyEmail(tfEmail.getText())) {

                    // Valida se o CPF digitado é um CPF válido/real
                    if (CPFValidator.validateCPF(tfCPF.getText())) {

                        // Verifica se já existe alguma pessoa usuária cadastrada com esse CPF no banco de dados
                        if (new VerifyDAO().verifyCPF(tfCPF.getText())) {
                            // Pergunto se a pessoa deseja mesmo realizar o cadastro
                            int opcao = JOptionPane.showOptionDialog(null, "Tem certeza que deseja cadastrar um novo usuário?", "Confirmação final",
                                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, 0);

                            // Se o botão sim for apertado, cadastramos o novo usuário
                            if (opcao == 0) {
                                pessoa.setNome(tfNome.getText());
                                pessoa.setEmail(tfEmail.getText());
                                pessoa.setEmpresa(tfEmpresa.getText());
                                pessoa.setCargo(tfCargo.getText());
                                pessoa.setEndereco(tfEndereco.getText());
                                pessoa.setCidade(tfCidade.getText());
                                pessoa.setCep(tfCEP.getText());
                                pessoa.setCPF(tfCPF.getText());
                                pessoa.setTelefone(tfTelefone.getText());

                                PessoaDAO.save(pessoa);

                                tfNome.setText(null);
                                tfEmail.setText(null);
                                tfEmpresa.setText(null);
                                tfCargo.setText(null);
                                tfEndereco.setText(null);
                                tfCidade.setText(null);
                                tfCEP.setText(null);
                                tfCPF.setText(null);
                                tfTelefone.setText(null);

                            }
                            else {
                                JOptionPane.showMessageDialog(null, "Já existe um usuário com esse username cadastrado\n Digite um username diferente, por favor",
                                        "Erro tentar realizar cadastro", JOptionPane.WARNING_MESSAGE);
                            }
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "Esse CPF já foi cadastrado em algum usuário!",
                                    "Erro tentar realizar cadastro", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "O CPF digitado é inválido\nPor favor, digite um CPF válido/correto!",
                                "Erro tentar realizar cadastro", JOptionPane.WARNING_MESSAGE);
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null, "O E-mail digitado já está em uso\nPor favor, digite tente com outro E-mail",
                            "Erro tentar realizar cadastro", JOptionPane.WARNING_MESSAGE);
                }
            }
            else {
                JOptionPane.showMessageDialog(null, "O E-mail digitado é inválido\nPor favor, digite um E-mail válido/correto!",
                        "Erro tentar realizar cadastro", JOptionPane.WARNING_MESSAGE);
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos, por favor!",
                    "Erro tentar realizar cadastro", JOptionPane.WARNING_MESSAGE);
        }
    }

    @FXML
    void btnCancelarMouseClicked(MouseEvent event) {
        tfNome.setText(null);
        tfEmail.setText(null);
        tfEmpresa.setText(null);
        tfCargo.setText(null);
        tfEndereco.setText(null);
        tfCidade.setText(null);
        tfCEP.setText(null);
        tfCPF.setText(null);
        tfTelefone.setText(null);
    }

    @FXML
    void tfCEPKeyReleased(KeyEvent event) {
        TextFieldFormatter tff = new TextFieldFormatter();
        tff.setMask("#####-###");
        tff.setCaracteresValidos("0123456789");
        tff.setTf(tfCEP);
        tff.formatter();
    }

    @FXML
    void tfCPFKeyReleased(KeyEvent event) {
        TextFieldFormatter tff = new TextFieldFormatter();
        tff.setMask("###.###.###-##");
        tff.setCaracteresValidos("0123456789");
        tff.setTf(tfCPF);
        tff.formatter();
    }

    @FXML
    void tfTelefoneOnKeyReleased(KeyEvent event) {
        TextFieldFormatter tff = new TextFieldFormatter();
        tff.setMask("(##)#####-####");
        tff.setCaracteresValidos("0123456789");
        tff.setTf(tfTelefone);
        tff.formatter();
    }

}
