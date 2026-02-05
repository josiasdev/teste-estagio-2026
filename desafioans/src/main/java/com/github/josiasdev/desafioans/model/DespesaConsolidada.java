package com.github.josiasdev.desafioans.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DespesaConsolidada {
    private String cnpj;
    private String razaoSocial;
    private Integer trimestre;
    private Integer ano;
    private BigDecimal valorDespesas;
    private String statusIntegridade;
}