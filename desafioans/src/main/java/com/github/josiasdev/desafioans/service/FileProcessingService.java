package com.github.josiasdev.desafioans.service;

import com.github.josiasdev.desafioans.config.AppConfig;
import com.github.josiasdev.desafioans.model.DespesaConsolidada;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.RFC4180ParserBuilder;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

@Service
public class FileProcessingService {

    public List<DespesaConsolidada> processarArquivosBaixados() throws Exception {
        List<DespesaConsolidada> consolidado = new ArrayList<>();
        File downloadDir = new File(AppConfig.DOWNLOAD_DIR);
        File[] zips = downloadDir.listFiles((dir, name) -> name.endsWith(".zip"));

        if (zips == null) return consolidado;

        for (File zip : zips) {
            String extraidoPath = extrairEFiltrar(zip);
            if (extraidoPath != null) {
                consolidado.addAll(lerENormalizarCsv(extraidoPath));
            }
        }
        return consolidado;
    }

    private String extrairEFiltrar(File zip) throws IOException {
        try (ZipFile zipFile = new ZipFile(zip)) {
            Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();
            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = entries.nextElement();
                String fileName = entry.getName().toLowerCase();

                if (fileName.endsWith(".csv") && !entry.isDirectory()) {
                    File target = new File(AppConfig.EXTRACT_DIR, entry.getName());
                    try (InputStream is = zipFile.getInputStream(entry);
                         OutputStream os = new FileOutputStream(target)) {
                        is.transferTo(os);
                    }
                    return target.getAbsolutePath();
                }
            }
        }
        return null;
    }

    private List<DespesaConsolidada> lerENormalizarCsv(String path) throws Exception {
        List<DespesaConsolidada> resultados = new ArrayList<>();

        try (Reader reader = Files.newBufferedReader(Paths.get(path), StandardCharsets.ISO_8859_1);
             CSVReader csvReader = new CSVReaderBuilder(reader)
                     .withCSVParser(new com.opencsv.RFC4180ParserBuilder()
                             .withSeparator(';')
                             .build())
                     .build()) {

            String[] header = csvReader.readNext();
            if (header == null) return resultados;

            Map<String, Integer> colMap = mapearCabecalho(header);

            String[] linha;
            while ((linha = csvReader.readNext()) != null) {
                if (linha.length <= Collections.max(colMap.values())) continue;

                try {
                    String conta = linha[colMap.get("CONTA")];
                    String descricao = linha[colMap.get("DESCRICAO")].toUpperCase();

                    if (conta.startsWith("411") || descricao.contains("EVENTOS") || descricao.contains("SINISTROS")) {

                        BigDecimal valor = converterValor(linha[colMap.get("VALOR")]);

                        if (valor.compareTo(BigDecimal.ZERO) > 0) {
                            resultados.add(DespesaConsolidada.builder()
                                    .cnpj(linha[colMap.get("REG_ANS")])
                                    .razaoSocial("Operadora: " + linha[colMap.get("REG_ANS")])
                                    .valorDespesas(valor)
                                    .trimestre(extrairTrimestreDaData(linha[colMap.get("DATA")]))
                                    .ano(2025)
                                    .build());
                        }
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }
        return resultados;
    }

    private Map<String, Integer> mapearCabecalho(String[] header) {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < header.length; i++) {
            String h = header[i].toUpperCase().trim();
            if (h.equals("REG_ANS")) map.put("REG_ANS", i);
            if (h.contains("CONTA_CONTABIL")) map.put("CONTA", i);
            if (h.equals("DESCRICAO")) map.put("DESCRICAO", i);
            if (h.equals("VL_SALDO_FINAL")) map.put("VALOR", i);
            if (h.equals("DATA")) map.put("DATA", i);
        }
        return map;
    }

    private BigDecimal converterValor(String valor) {
        if (valor == null || valor.isEmpty()) return BigDecimal.ZERO;
        String limpo = valor.replace(".", "").replace(",", ".");
        return new BigDecimal(limpo);
    }

    private Integer extrairTrimestreDaData(String data) {
        if (data == null || data.length() < 7) return 1;
        String mes = data.substring(5, 7);
        return (Integer.parseInt(mes) - 1) / 3 + 1;
    }
}