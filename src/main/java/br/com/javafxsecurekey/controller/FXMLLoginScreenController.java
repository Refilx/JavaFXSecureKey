package br.com.javafxsecurekey.controller;

import br.com.javafxsecurekey.model.aplication.FXMLLoginScreenAplication;
import br.com.javafxsecurekey.model.aplication.FXMLNavigationPanelAplication;
import br.com.javafxsecurekey.model.dao.UsuarioDAO;
import br.com.javafxsecurekey.model.dao.VerifyDAO;
import br.com.javafxsecurekey.model.domain.Usuario;
import br.com.javafxsecurekey.model.util.CaseTextFormatter;
import br.com.javafxsecurekey.model.util.Email;
import br.com.javafxsecurekey.model.util.TextFieldFormatter;
import br.com.javafxsecurekey.model.validator.EmailValidator;
import br.com.javafxsecurekey.model.validator.LoginValidator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Classe Controladora da tela de login e das telas de recuperação de senha
 * @author Bruno Sousa da Silva
 */
public class FXMLLoginScreenController implements Initializable {

    // -----------------------TELA PRINCIPAL DE LOGIN-----------------------
    @FXML
    private AnchorPane loginScreen;
    @FXML
    private VBox VBoxFields;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Pane bottomPane;
    @FXML
    private Button btnEntrar;
    @FXML
    private Pane leftPane;
    @FXML
    private ImageView logoImgView;
    @FXML
    private PasswordField passFieldSenha;
    @FXML
    private Pane rightPane;
    @FXML
    private Label senhaLabel;
    @FXML
    private TextField textFieldUsername;
    @FXML
    private Pane topPane;
    @FXML
    private Label usernameLabel;

    private Email email = new Email();
    private static Usuario dadosDoUsuario = new Usuario();
    private static String codigoEnviado;
    private Alert alert;

    // -----------------------TELA INSERÇÃO DO EMAIL DE RECUPERAÇÃO-----------------------
    @FXML
    private Button btnRecuperar;
    @FXML
    private Button btnCancelar;
    @FXML
    private TextField textFieldEmailToRecoverPassword;

    // -----------------------TELA INSERÇÃO DO CÓDIGO DE RECUPERAÇÃO-----------------------
    @FXML
    private TextField tfRecoveryCode;

    // -----------------------TELA DE REDEFINIÇÃO DE SENHA-----------------------
    @FXML
    private PasswordField pfConfirmNovaSenha;
    @FXML
    private PasswordField pfNovaSenha;

    // -----------------------INICIALIZE E BOTÃO DA TELA DE LOGIN-----------------------
    @Override
    public void initialize(URL location, ResourceBundle resourses) {
        CaseTextFormatter.applyLowerCase(textFieldUsername);
    }

    @FXML
    private void btnEntrarOnMouseClicked(MouseEvent event) throws IOException {
        //Pegamos o conteúdo do campo username da tela de login e armazenamos na variável user
        String username = textFieldUsername.getText();

        //Inicializamos a String que pegará o valor do campo password da tela de login
        String password = passFieldSenha.getText();

        boolean resultVerify = LoginValidator.resultVerify(username, password);

        if(resultVerify){

            Stage stageAtual = (Stage) ((Node) event.getSource()).getScene().getWindow();

            //FXMLLoginScreenAplication loginScreen = new FXMLLoginScreenAplication();
            FXMLNavigationPanelAplication navPanel = new FXMLNavigationPanelAplication();

            if (new VerifyDAO().verifySuperUser()) {
                stageAtual.close(); // fecha a tela de login

                // Chama a tela de Navegação do usuário admininstrador
                navPanel.start(new Stage());
            } else {
                stageAtual.close(); // fecha a tela de login

                // Chama a tela de Navegação do usuário comum
                navPanel.trocarDeTela(new Stage(), "FXMLCommonUserNavigationPanel");
            }
        }
    }



    // --------- NAVEGAÇÃO ENTRE TELAS DE RECUPERAÇÃO E LOGIN ----------
    @FXML
    private void getLoginScreen(MouseEvent event) throws IOException {
        loadPage("FXMLLoginScreen");
    }

    @FXML
    private void getEmailToRecoverPasswordScreen(MouseEvent event) throws IOException {
        loadPage("FXMLEmailToRecoverPasswordScreen");
    }

    @FXML
    private void getEnterRecoveryCodeScreen(MouseEvent event) throws IOException {
        loadPage("FXMLEnterRecoveryCodeScreen");
    }

    @FXML
    private void getEnterNewPasswordScreen(MouseEvent event) throws IOException {
        loadPage("FXMLEnterNewPasswordScreen");
    }

    public void loadPage(String page) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/br/com/javafxsecurekey/view/screens/"+page+".fxml"));
        Parent root = fxmlLoader.load();

        borderPane.setCenter(root);
    }

    // -----------------------BOTÃO DA TELA EMAIL DE RECUPERAÇÃO-----------------------
    @FXML
    void btnRecuperarOnMouseClicked(MouseEvent event) throws IOException {

        // Verifica se o e-mail digitado é válido
        if(EmailValidator.isValidEmail(textFieldEmailToRecoverPassword.getText())) {

            // Verifica se o e-mail digitado está cadastrado no banco de dados
            if(VerifyDAO.verifyEmail(textFieldEmailToRecoverPassword.getText())) {

                // Armazenando o email do usuário e buscando o ID desse usuário no banco de dados e guardando nos dados do usuário
                dadosDoUsuario.setEmail(textFieldEmailToRecoverPassword.getText());
                UsuarioDAO.getIdUsuarioByEmail(dadosDoUsuario);

                // Enviando email com o cógido de recuperação de senha para o e-mail do usuário
                boolean enviado = email.sendMailTo(textFieldEmailToRecoverPassword.getText());

                if(enviado) {
                    codigoEnviado = email.getRecoveryCode(); //Guardo o código de recuperação enviado
                    getEnterRecoveryCodeScreen(null); // Chamando a tela de inserção do código de recuperação
                }
            }
            else {
                alert = new Alert(Alert.AlertType.WARNING, "Erro tentar realizar o envio do email de recuperação\n\nO E-mail digitado não está cadastrado em nenhum usuário!");
                alert.showAndWait();
            }
        }
        else {
            alert = new Alert(Alert.AlertType.INFORMATION, "Erro tentar realizar o envio do email de recuperação\n\nO E-mail digitado é inválido\nPor favor, digite um E-mail válido/correto!");
            alert.showAndWait();
        }
    }

    // -----------------------BOTÃO DA TELA CÓDIGO DE RECUPERAÇÃO-----------------------
    @FXML
    void btnConfirmarRecoveryCodeOnMouseClicked(MouseEvent event) throws IOException {
        if(codigoEnviado.equals(tfRecoveryCode.getText())) {
            alert = new Alert(Alert.AlertType.INFORMATION,"Código inserido validado com sucesso!");
            alert.showAndWait();
            getEnterNewPasswordScreen(null);
        }
        else {
            alert = new Alert(Alert.AlertType.WARNING,"Código digitado errado!\n\nO código digitado é diferente do enviado!\nDigite o código enviado corretamente");
            alert.showAndWait();
        }
    }

    // -----------------------BOTÃO DA TELA REDEFINIÇÃO DE SENHA-----------------------
    @FXML
    void btnSalvarNovaSenhaOnMouseClicked(MouseEvent event) throws IOException {
        if(pfNovaSenha.getText().equals(pfConfirmNovaSenha.getText())) {
            dadosDoUsuario.setPassword(pfNovaSenha.getText());
            UsuarioDAO.updateSenhaDoUsuario(dadosDoUsuario);
            getLoginScreen(null);
        }
        else {
            alert = new Alert(Alert.AlertType.WARNING, "Erro tentar realizar atualizar senhas\n\nAs senhas estão diferentes!\nOs campos precisam estar com senhas iguais!");
            alert.showAndWait();
            pfNovaSenha.setText(null);
            pfConfirmNovaSenha.setText(null);
        }
    }

    // -----------------------MASCARA DO CAMPO DA TELA CÓDIGO DE RECUPERAÇÃO-----------------------
    @FXML
    void tfRecoveryCodeOnKeyReleased(KeyEvent event) {
        TextFieldFormatter tff = new TextFieldFormatter();
        tff.setMask("****-****-****");
        tff.setTf(tfRecoveryCode);
        tff.formatter();
    }

}

