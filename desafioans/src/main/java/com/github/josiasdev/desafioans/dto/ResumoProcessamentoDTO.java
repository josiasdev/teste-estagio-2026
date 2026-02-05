package com.github.josiasdev.desafioans.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class ResumoProcessamentoDTO {
    private String status;
    private int totalArquivosBaixados;
    private long totalRegistrosProcessados;
    private String caminhoArquivoFinal;
    private List<String> alertas;
}