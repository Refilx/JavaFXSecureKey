package br.com.javafxsecurekey.model.util;

import javafx.scene.control.Alert;
import org.apache.commons.mail.*;

import javax.swing.*;
import java.util.UUID;

public class Email {

    private String recoveryCode;
    private Alert alert;

    public boolean sendMailTo(String destinatario) {
        // Dependências necessárias: commons-email-1.5 e mail-1.6.2

        boolean enviado = false;

        String[] code = generateUniqueCode().split("-");
        recoveryCode = code[1]+"-"+code[2]+"-"+code[3];

        String msg = "Recebemos uma solicitação para recuperar a senha da sua conta.\n" +
                "Favor usar o código %s somente válido enquanto a tela de inserção do código estiver aberta.\n".formatted(recoveryCode) +
                "Se você não solicitou a recuperação da sua senha, favor ignorar essa mensagem.\n\n" +
                "---------------\n" +
                "PassaBus Administration";

        String appMail = "";
        String appMailPass = ""; // Senha de app gerada pelo google

        SimpleEmail email = new SimpleEmail();
        email.setHostName("smtp.gmail.com");
        email.setSmtpPort(587);  // Porta 587 para STARTTLS
        email.setSSLOnConnect(false);  // Desabilitar SSL direto
        email.setStartTLSEnabled(true); // Habilitar STARTTLS
        email.setAuthenticator(new DefaultAuthenticator(appMail, appMailPass));

        try {
            email.setFrom(appMail);
            email.setSubject("Recuperação de senha | Secure Key | Não Responda");
            email.setMsg(msg);
            email.addTo(destinatario);
            email.send();
            alert = new Alert(Alert.AlertType.INFORMATION, "Email de recuperação enviado com sucesso!\nFavor confira sua caixa de entrada!");
            alert.showAndWait();
            enviado = true;
        } catch (EmailException e) {
            // Tratamento para outros erros de envio de e-mail
            JOptionPane.showMessageDialog(null, "Erro ao enviar o e-mail. \nDetalhes: "+ e.getMessage());
            enviado = false;
        } catch (Exception e) {
            // Para qualquer outro tipo de erro
            JOptionPane.showMessageDialog(null, "Ocorreu um erro inesperado. \nDetalhes: " + e.getMessage());
        }
        return enviado;
    }

    // Gerar um código único
    public static String generateUniqueCode() {
        UUID uniqueKey = UUID.randomUUID(); // Gera um UUID aleatório
        return uniqueKey.toString(); // Retorna o código como uma string
    }

    public String getRecoveryCode() {
        return recoveryCode;
    }


    public void emailComAnexo(ByteArrayDataSource dataSource) {
        // Criar o anexo para o e-mail com o PDF gerado em memória
        EmailAttachment anexo = new EmailAttachment();
        anexo.setDisposition(EmailAttachment.ATTACHMENT); // Tipo de anexo
        anexo.setDescription("Relatório de Passagem"); // Descrição do anexo
        anexo.setName("RelatorioPassagem.pdf"); // Nome do arquivo anexo
        anexo.setPath(dataSource.getName());  // Isso não é correto para um ByteArrayDataSource. Então, vamos tratar de outra maneira.

        String appMail = "";
        String appMailPass = ""; // Senha de app gerada pelo google

        MultiPartEmail email = new MultiPartEmail();
        email.setHostName("smtp.gmail.com");
        email.setSmtpPort(587);  // Porta 587 para STARTTLS
        email.setSSLOnConnect(false);  // Desabilitar SSL direto
        email.setStartTLSEnabled(true); // Habilitar STARTTLS
        email.setAuthenticator(new DefaultAuthenticator(appMail, appMailPass));

        try {
            email.setFrom(appMail);
            email.setSubject("Email com Anexo | Secure Key | Não Responda");
            email.setMsg("Testando envio de anexo");
            email.addTo("");

            // Adicionando o ByteArrayDataSource ao anexo com o método correto
            email.attach(dataSource, "RelatorioPassagem.pdf", "Relatório de Passagem");

            // Enviando email
            email.send();
        } catch (EmailException e) {
            // Tratamento para outros erros de envio de e-mail
            JOptionPane.showMessageDialog(null, "Erro ao enviar o e-mail. \nDetalhes: "+ e.getMessage());
        } catch (Exception e) {
            // Para qualquer outro tipo de erro
            JOptionPane.showMessageDialog(null, "Ocorreu um erro inesperado. \nDetalhes: " + e.getMessage());
        }
    }
}
