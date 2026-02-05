package com.github.josiasdev.desafioans.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Configuration
public class AppConfig {
    public static final String DOWNLOAD_DIR = "temp/ans/downloads";
    public static final String EXTRACT_DIR = "temp/ans/extracted";

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @PostConstruct
    public void setupDirectories() {
        try {
            Files.createDirectories(Paths.get(DOWNLOAD_DIR));
            Files.createDirectories(Paths.get(EXTRACT_DIR));
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível criar os diretórios temporários", e);
        }
    }
}
