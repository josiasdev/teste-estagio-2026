package com.github.josiasdev.desafio_ans_analise.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class OperadoraCadastroDTO {
    @CsvBindByName(column = "CNPJ")
    private String cnpj;

    @CsvBindByName(column = "REGISTRO_OPERADORA")
    private String registroAns;

    @CsvBindByName(column = "Razao_Social")
    private String razaoSocial;

    @CsvBindByName(column = "Modalidade")
    private String modalidade;

    @CsvBindByName(column = "UF")
    private String uf;
}