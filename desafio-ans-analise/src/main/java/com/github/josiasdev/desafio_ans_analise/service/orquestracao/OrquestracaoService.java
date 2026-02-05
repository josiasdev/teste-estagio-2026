package com.github.josiasdev.desafio_ans_analise.service.orquestracao;

import com.github.josiasdev.desafio_ans_analise.dto.RegistroConsolidadoDTO;
import com.github.josiasdev.desafio_ans_analise.dto.OperadoraCadastroDTO;
import com.github.josiasdev.desafio_ans_analise.service.File.FileService;
import com.github.josiasdev.desafio_ans_analise.service.aggregation.AgregacaoService;
import com.github.josiasdev.desafio_ans_analise.service.ans.ANSCadastroCrawlerService;
import com.github.josiasdev.desafio_ans_analise.service.enrichment.EnriquecimentoService;
import com.github.josiasdev.desafio_ans_analise.service.validation.ValidadorRegistro;
import org.springframework.stereotype.Service;
import com.github.josiasdev.desafio_ans_analise.config.AppConfig;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrquestracaoService {

    private final ANSCadastroCrawlerService cadastroCrawler;
    private final FileService fileService;
    private final EnriquecimentoService enriquecimentoService;
    private final AgregacaoService agregacaoService;
    private final List<ValidadorRegistro> validadores;

    public OrquestracaoService(ANSCadastroCrawlerService cadastroCrawler, FileService fileService,
                               EnriquecimentoService enriquecimentoService,
                               AgregacaoService agregacaoService,
                               List<ValidadorRegistro> validadores) {
        this.cadastroCrawler = cadastroCrawler;
        this.fileService = fileService;
        this.enriquecimentoService = enriquecimentoService;
        this.agregacaoService = agregacaoService;
        this.validadores = validadores;
    }

    public String executarFluxoDesafio2() throws Exception {

        Files.createDirectories(Paths.get(AppConfig.DOWNLOAD_DIR));
        Files.createDirectories(Paths.get(AppConfig.OUTPUT_DIR));
        cadastroCrawler.baixarCadastroOperadoras();

        Path pathConsolidado = Paths.get("consolidado.csv");

        if (!Files.exists(pathConsolidado)) {
            throw new Exception("Arquivo 'consolidado.csv' não encontrado na raiz: " + pathConsolidado.toAbsolutePath());
        }

        List<RegistroConsolidadoDTO> despesas = fileService.lerCsvConsolidado(pathConsolidado);

        List<OperadoraCadastroDTO> cadastro = fileService.lerCsvCadastro(
                Paths.get(AppConfig.DOWNLOAD_DIR, "Relatorio_cadop.csv"));

//        List<RegistroConsolidadoDTO> validados = despesas.stream()
//                .filter(d -> validadores.stream().allMatch(v -> v.isValid(d)))
//                .collect(Collectors.toList());

        List<RegistroConsolidadoDTO> validados = despesas;

        var enriquecidos = enriquecimentoService.enriquecer(validados, cadastro);

        var agregados = agregacaoService.agruparECalcularEstatisticas(enriquecidos);

        String csvFinal = "despesas_agregadas.csv";
        fileService.salvarCsvAgregado(agregados, csvFinal);
        fileService.compactarParaZip("Teste_Josias.zip", csvFinal);

        return "Teste_Josias.zip";
    }
}