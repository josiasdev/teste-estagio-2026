package com.github.josiasdev.desafio_ans_analise.dto;


import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class DespesaAgregadaDTO {
    private String razaoSocial;
    private String uf;
    private BigDecimal valorTotal;
    private BigDecimal mediaTrimestral;
    private BigDecimal desvioPadrao;
}