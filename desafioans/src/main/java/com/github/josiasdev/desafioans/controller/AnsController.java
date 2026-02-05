package com.github.josiasdev.desafioans.controller;

import com.github.josiasdev.desafioans.dto.ProcessamentoStatusDTO;
import com.github.josiasdev.desafioans.service.ANSCrawlerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/ans")
@Tag(name = "ANS Consolidador", description = "Endpoints para processamento de dados da ANS")
public class AnsController {

    private final ANSCrawlerService crawlerService;

    public AnsController(ANSCrawlerService crawlerService) {
        this.crawlerService = crawlerService;
    }

    @PostMapping("/consolidar")
    @Operation(summary = "Inicia o crawler, baixa os arquivos e gera o ZIP consolidado")
    public ResponseEntity<ProcessamentoStatusDTO> iniciarConsolidacao() {
        try {
            crawlerService.executarFluxoCompleto();
            return ResponseEntity.ok(new ProcessamentoStatusDTO(
                    "Processamento concluído com sucesso!",
                    LocalDateTime.now(),
                    true,
                    "consolidado_despesas.zip"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ProcessamentoStatusDTO(
                    "Erro no processamento: " + e.getMessage(),
                    LocalDateTime.now(),
                    false,
                    null
            ));
        }
    }
}