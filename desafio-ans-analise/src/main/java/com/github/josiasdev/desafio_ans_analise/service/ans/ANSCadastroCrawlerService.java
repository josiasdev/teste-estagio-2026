package com.github.josiasdev.desafio_ans_analise.service.ans;

import com.github.josiasdev.desafio_ans_analise.config.AppConfig;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ANSCadastroCrawlerService {

    private final RestTemplate restTemplate;
    private static final String CADASTRO_URL = "https://dadosabertos.ans.gov.br/FTP/PDA/operadoras_de_plano_de_saude_ativas//Relatorio_cadop.csv";

    public ANSCadastroCrawlerService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Path baixarCadastroOperadoras() throws Exception {
        Path diretorio = Paths.get(AppConfig.DOWNLOAD_DIR);
        if (Files.notExists(diretorio)) {
            Files.createDirectories(diretorio);
        }

        Path destino = diretorio.resolve("Relatorio_cadop.csv");

        System.out.println("Iniciando download do cadastro: " + CADASTRO_URL);

        restTemplate.execute(CADASTRO_URL, HttpMethod.GET, null, response -> {
            Files.copy(response.getBody(), destino, StandardCopyOption.REPLACE_EXISTING);
            return destino;
        });

        System.out.println("Download do cadastro concluído com sucesso.");
        return destino;
    }
}