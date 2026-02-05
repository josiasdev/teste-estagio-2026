package com.github.josiasdev.desafioans.service;

import com.github.josiasdev.desafioans.config.AppConfig;
import com.github.josiasdev.desafioans.model.ConsolidationService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ANSCrawlerService {
    private final RestTemplate restTemplate;
    private final FileProcessingService fileProcessingService;
    private final ConsolidationService consolidationService;
    private static final String BASE_URL = "https://dadosabertos.ans.gov.br/FTP/PDA/demonstracoes_contabeis/";

    public ANSCrawlerService(RestTemplate restTemplate, FileProcessingService fileProcessingService, ConsolidationService consolidationService) {
        this.restTemplate = restTemplate;
        this.fileProcessingService = fileProcessingService;
        this.consolidationService = consolidationService;
    }

    public void executarFluxoCompleto() throws Exception {
        baixarArquivos2025Direto(); // Método ajustado para a estrutura atual
        var dados = fileProcessingService.processarArquivosBaixados();
        if (!dados.isEmpty()) {
            consolidationService.consolidar(dados);
        } else {
            System.out.println("Aviso: Nenhum dado foi processado. Verifique o separador do CSV.");
        }
    }

    private void baixarArquivos2025Direto() throws Exception {
        String url2025 = BASE_URL + "2025/";
        Document doc = Jsoup.connect(url2025).get();

        // Busca arquivos que terminam com T2025.zip (1T2025, 2T2025...)
        List<String> zips = doc.select("a[href]").stream()
                .map(link -> link.attr("href"))
                .filter(href -> href.matches("\\dT2025\\.zip"))
                .collect(Collectors.toList());

        for (String zipName : zips) {
            efetuarDownload(url2025 + zipName);
        }
    }

    private void efetuarDownload(String url) throws Exception {
        String nome = url.substring(url.lastIndexOf("/") + 1);
        Path dir = Paths.get(AppConfig.DOWNLOAD_DIR);
        if (Files.notExists(dir)) {
            Files.createDirectories(dir);
        }
        Path destino = dir.resolve(nome);
        restTemplate.execute(url, org.springframework.http.HttpMethod.GET, null, response -> {
            Files.copy(response.getBody(), destino, StandardCopyOption.REPLACE_EXISTING);
            return destino;
        });
        System.out.println("Baixado: " + nome);
    }
}