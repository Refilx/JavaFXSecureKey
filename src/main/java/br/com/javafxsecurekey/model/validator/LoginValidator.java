package br.com.javafxsecurekey.model.validator;

import br.com.javafxsecurekey.model.dao.VerifyDAO;
import javafx.scene.control.Alert;

public class LoginValidator {

    public static boolean resultVerify(String user, String pass) {

        boolean resultVerify = false;

        //Verificamos se os campos de usuário e senha estão devidamente preenchidos
        if(!user.isBlank() && !pass.isBlank()){
            VerifyDAO verifyDAO= new VerifyDAO();

            //Verificação se o usuário ou a senha fornecida estão corretos
            resultVerify = verifyDAO.verifyPass(user, pass);

            if(resultVerify) {
                //Mensagem de login bem sucedido
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Login Realizado com Sucesso");
                alert.showAndWait();
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING, "Erro ao tentar realizar o login\n\nPreencha todos os campos, por favor!");
            alert.showAndWait();
        }

        return resultVerify;
    }
}
