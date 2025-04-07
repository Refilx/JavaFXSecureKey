package br.com.javafxsecurekey.model.validator;

public class CPFValidator {

    public static boolean validateCPF(String cpf) {
        // Remove caracteres não numéricos
        cpf = cpf.replaceAll("[^0-9]", "");

        // Verifica se o CPF tem 11 dígitos
        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        // Calcula o primeiro dígito verificador
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += (10 - i) * Character.getNumericValue(cpf.charAt(i));
        }
        int firstVerifier = 11 - (sum % 11);
        if (firstVerifier >= 10) {
            firstVerifier = 0;
        }
        if (firstVerifier != Character.getNumericValue(cpf.charAt(9))) {
            return false;
        }

        // Calcula o segundo dígito verificador
        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += (11 - i) * Character.getNumericValue(cpf.charAt(i));
        }
        int secondVerifier = 11 - (sum % 11);
        if (secondVerifier >= 10) {
            secondVerifier = 0;
        }
        return secondVerifier == Character.getNumericValue(cpf.charAt(10));
    }

    public static void main(String[] args) {
        String cpf = "123.456.789-09"; // Exemplo de CPF
        if (validateCPF(cpf)) {
            System.out.println("CPF é válido.");
        } else {
            System.out.println("CPF é inválido.");
        }
    }
}