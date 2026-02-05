package com.github.josiasdev.desafioans.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ProcessamentoStatusDTO {
    private String mensagem;
    private LocalDateTime timestamp;
    private boolean sucesso;
    private String arquivoGerado;
}