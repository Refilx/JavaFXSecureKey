package br.com.javafxsecurekey.model.util;

import br.com.javafxsecurekey.model.factory.ConnectionFactory;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.commons.mail.ByteArrayDataSource;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class EnviarRelatorioPorEmail {

    public void gerarRelatorioEnviarPorEmail() throws Exception {
        // Gerar o relatório em PDF diretamente em memória
        InputStream logoStream = getClass().getResourceAsStream("/br/com/passabus/view/imgs/logo.png");
        InputStream mainStream = getClass().getResourceAsStream("/br/com/passabus/view/imgs/mainbus.png");

        if (logoStream == null) {
            throw new RuntimeException("Imagem não encontrada: logo.png");
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("logo", logoStream);
        parameters.put("mainbus", mainStream);

        InputStream jasperStream = getClass().getResourceAsStream("/br/com/passabus/relatorios/JAVAFXFXMLRelatórioPassagem.jasper");

        if (jasperStream == null) {
            throw new JRException("Arquivo do relatório não encontrado!");
        }

        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, ConnectionFactory.createConnectionToMySQL());

        // Gerar o PDF diretamente para um ByteArrayOutputStream
        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, pdfOutputStream);

        // Usar ByteArrayDataSource para criar o anexo sem salvar o arquivo fisicamente
        ByteArrayDataSource dataSource = new ByteArrayDataSource(pdfOutputStream.toByteArray(), "application/pdf");

        Email email = new Email();
        email.emailComAnexo(dataSource);
    }

    public static void main(String[] args) throws Exception {
        EnviarRelatorioPorEmail enviarRelatorioPorEmail = new EnviarRelatorioPorEmail();
        enviarRelatorioPorEmail.gerarRelatorioEnviarPorEmail();
    }
}
