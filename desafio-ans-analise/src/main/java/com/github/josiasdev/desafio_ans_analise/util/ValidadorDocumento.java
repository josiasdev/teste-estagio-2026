package com.github.josiasdev.desafio_ans_analise.util;

public class ValidadorDocumento {
    public static boolean isCnpjValido(String cnpj) {
        if (cnpj == null) return false;

        String limpo = cnpj.replaceAll("\\D", "");

        if (limpo.length() != 14 || limpo.matches("(\\d)\\1{13}")) {
            return false;
        }

        try {
            String base = limpo.substring(0, 12);
            String digito1 = calcularDigito(base, new int[]{5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2});
            String digito2 = calcularDigito(base + digito1, new int[]{6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2});

            return limpo.equals(base + digito1 + digito2);
        } catch (Exception e) {
            return false;
        }
    }

    private static String calcularDigito(String str, int[] pesos) {
        int soma = 0;
        for (int i = str.length() - 1, j = 0; i >= 0; i--, j++) {
            soma += Character.getNumericValue(str.charAt(i)) * pesos[pesos.length - 1 - j];
        }
        int resto = soma % 11;
        int resultado = (resto < 2) ? 0 : 11 - resto;
        return String.valueOf(resultado);
    }
}