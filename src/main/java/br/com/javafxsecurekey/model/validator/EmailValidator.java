package br.com.javafxsecurekey.model.validator;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * Classe que valida se o Email informado é válido
 * @author Bruno Sousa da Silva
 */
public class EmailValidator {
    public static boolean isValidEmail(String email) {
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
            return true;  // email válido
        } catch (AddressException ex) {
            return false;  // email inválido
        }
    }

    public static void main(String[] args) {
        System.out.println(isValidEmail("teste@example.com"));  // true
        System.out.println(isValidEmail("teste.com"));          // false
    }
}
