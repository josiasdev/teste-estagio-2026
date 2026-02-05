package com.github.josiasdev.desafio_ans_analise.controller;

import com.github.josiasdev.desafio_ans_analise.service.orquestracao.OrquestracaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ans/v2")
@Tag(name = "ANS Desafio 2", description = "Transformação, Enriquecimento e Agregação de Dados")
public class AnsController {

    private final OrquestracaoService orquestracaoService;

    public AnsController(OrquestracaoService orquestracaoService) {
        this.orquestracaoService = orquestracaoService;
    }

    @PostMapping("/processar")
    @Operation(summary = "Inicia o download do cadastro, valida CNPJs, faz join com despesas e gera o ZIP agregado")
    public ResponseEntity<String> processarDados() {
        try {
            String arquivoGerado = orquestracaoService.executarFluxoDesafio2();

            return ResponseEntity.ok("Processamento concluído com sucesso! Arquivo gerado: " + arquivoGerado);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("Erro ao processar Desafio 2: " + e.getMessage());
        }
    }
}