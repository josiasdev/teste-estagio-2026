package com.github.josiasdev.desafio_ans_analise.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
    public static final String BASE_DIR = "dados_ans";
    public static final String DOWNLOAD_DIR = BASE_DIR + "/downloads";
    public static final String OUTPUT_DIR = BASE_DIR + "/output";

    public static final String ARQUIVO_CONSOLIDADO_ENTRADA = "consolidado.csv";

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
