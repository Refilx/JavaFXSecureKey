package br.com.javafxsecurekey.controller;

import br.com.javafxsecurekey.model.dao.PessoaDAO;
import br.com.javafxsecurekey.model.dao.UsuarioDAO;
import br.com.javafxsecurekey.model.dao.VerifyDAO;
import br.com.javafxsecurekey.model.domain.Usuario;
import br.com.javafxsecurekey.model.util.CaseTextFormatter;
import br.com.javafxsecurekey.model.util.TextFieldFormatter;
import br.com.javafxsecurekey.model.validator.CPFValidator;
import br.com.javafxsecurekey.model.validator.EmailValidator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import javax.swing.*;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class FXMLCadastroUserController implements Initializable {

    @FXML
    private ToggleGroup TipoUsuario;
    @FXML
    private Button btnCadastrar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Pane cadastroUserScreen;
    @FXML
    private PasswordField passFieldConfirmSenha;
    @FXML
    private PasswordField passFieldSenha;
    @FXML
    private RadioButton rb_admin;
    @FXML
    private RadioButton rb_gerente;
    @FXML
    private RadioButton rb_operador;
    @FXML
    private TextField textFieldCPF;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private TextField textFieldNome;
    @FXML
    private TextField textFieldTelefone;
    @FXML
    private TextField textFieldUsername;

    private DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static Usuario usuario = new Usuario();
    private static PessoaDAO pessoaDAO = new PessoaDAO();
    private static UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CaseTextFormatter.applyLowerCase(textFieldUsername);
    }

    @FXML
    void tfCPFKeyReleased(KeyEvent event) {
        TextFieldFormatter tff = new TextFieldFormatter();
        tff.setMask("###.###.###-##");
        tff.setCaracteresValidos("0123456789");
        tff.setTf(textFieldCPF);
        tff.formatter();
    }

    @FXML
    void textFieldTelefoneOnKeyReleased(KeyEvent event) {
        TextFieldFormatter tff = new TextFieldFormatter();
        tff.setMask("(##)#####-####");
        tff.setCaracteresValidos("0123456789");
        tff.setTf(textFieldTelefone);
        tff.formatter();
    }

    @FXML
    void btnCadastrarMouseClicked(MouseEvent event) {

        // Verifico se todos os campos estão preenchidos
        if(
                !textFieldNome.getText().isEmpty() &&
                        !textFieldEmail.getText().isEmpty() &&
                        !textFieldUsername.getText().isEmpty() &&
                        !textFieldTelefone.getText().isEmpty() &&
                        !textFieldCPF.getText().isEmpty() &&
                        !passFieldSenha.getText().isEmpty() &&
                        !passFieldConfirmSenha.getText().isEmpty() &&
                        (rb_admin.isSelected() || rb_gerente.isSelected() || rb_operador.isSelected())
        ) {
            // Verifica se o campo senha e comfimação de senha são iguais
            if(passFieldSenha.getText().equals(passFieldConfirmSenha.getText())) {

                // Valida o e-mail digitado para saber se é um e-mail válido
                if(EmailValidator.isValidEmail(textFieldEmail.getText())) {

                    // Verificar se o email já foi cadastrado no banco de dados
                    if(!VerifyDAO.verifyEmail(textFieldEmail.getText())) {

                        // Valida se o CPF digitado é um CPF válido/real
                        if (CPFValidator.validateCPF(textFieldCPF.getText())) {

                            // Verifica se já existe alguma pessoa usuária cadastrada com esse CPF no banco de dados
                            if (new VerifyDAO().verifyCPF(textFieldCPF.getText())) {

                                // Verifica se já existe alguma pessoa usuária cadastrada com esse username no banco de dados
                                if (new VerifyDAO().verifyUsername(textFieldUsername.getText())) {

                                    // Pergunto se a pessoa deseja mesmo realizar o cadastro
                                    int opcao = JOptionPane.showOptionDialog(null, "Tem certeza que deseja cadastrar um novo usuário?", "Confirmação final",
                                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sim", "Não"}, 0);

                                    // Se o botão sim for apertado, cadastramos o novo usuário
                                    if (opcao == 0) {
                                        usuario.setNome(textFieldNome.getText());
                                        usuario.setCPF(textFieldCPF.getText());
                                        usuario.setEmail(textFieldEmail.getText());
                                        usuario.setTelefone(textFieldTelefone.getText());

                                        usuario.setUsername(textFieldUsername.getText());
                                        usuario.setPassword(passFieldSenha.getText());
                                        if (rb_operador.isSelected())
                                            usuario.setRole("Operador");
                                        else if (rb_gerente.isSelected())
                                            usuario.setRole("Gerente");
                                        else if (rb_admin.isSelected())
                                            usuario.setRole("Administrador");

                                        pessoaDAO.save(usuario);

                                        usuarioDAO.save(usuario);

                                        textFieldNome.setText(null);
                                        textFieldUsername.setText(null);
                                        textFieldCPF.setText(null);
                                        textFieldEmail.setText(null);
                                        textFieldTelefone.setText(null);
                                        passFieldSenha.setText(null);
                                        passFieldConfirmSenha.setText(null);
                                    }
                                    else {
                                        JOptionPane.showMessageDialog(null, "O usuário NÃO foi cadastrado!",
                                                "Cancelamento do cadastro do usuário", JOptionPane.INFORMATION_MESSAGE);
                                        btnCancelarMouseClicked(null);
                                    }
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
                JOptionPane.showMessageDialog(null, "As senhas estão diferentes!",
                        "Erro tentar realizar cadastro", JOptionPane.WARNING_MESSAGE);
                passFieldSenha.setText(null);
                passFieldConfirmSenha.setText(null);
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos, por favor!",
                    "Erro tentar realizar cadastro", JOptionPane.WARNING_MESSAGE);
        }
    }

    @FXML
    void btnCancelarMouseClicked(MouseEvent event) {
        textFieldNome.setText(null);
        textFieldUsername.setText(null);
        textFieldCPF.setText(null);
        textFieldEmail.setText(null);
        textFieldTelefone.setText(null);
        passFieldSenha.setText(null);
        passFieldConfirmSenha.setText(null);
    }
}
