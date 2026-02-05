package com.github.josiasdev.desafio_ans_analise.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistroConsolidadoDTO {

    @CsvBindByName(column = "CNPJ")
    private String cnpj;

    @CsvBindByName(column = "RAZAOSOCIAL")
    private String razaoSocial;

    @CsvBindByName(column = "VALORDESPESAS")
    private BigDecimal valorDespesa;

    @CsvBindByName(column = "TRIMESTRE")
    private String trimestre;

    private String registroAns;
    private String modalidade;
    private String uf;
}
