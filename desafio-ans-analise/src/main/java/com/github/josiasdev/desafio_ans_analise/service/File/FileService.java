package com.github.josiasdev.desafio_ans_analise.service.File;

import com.github.josiasdev.desafio_ans_analise.config.AppConfig;
import com.github.josiasdev.desafio_ans_analise.dto.DespesaAgregadaDTO;
import com.github.josiasdev.desafio_ans_analise.dto.OperadoraCadastroDTO;
import com.github.josiasdev.desafio_ans_analise.dto.RegistroConsolidadoDTO;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class FileService {

    public void salvarCsvAgregado(List<DespesaAgregadaDTO> dados, String nomeArquivo) throws Exception {
        Path path = Paths.get(AppConfig.OUTPUT_DIR, nomeArquivo);
        Files.createDirectories(path.getParent());

        try (Writer writer = Files.newBufferedWriter(path)) {
            StatefulBeanToCsv<DespesaAgregadaDTO> beanToCsv = new StatefulBeanToCsvBuilder<DespesaAgregadaDTO>(writer)
                    .withSeparator(';')
                    .build();
            beanToCsv.write(dados);
        }
    }

    public void compactarParaZip(String nomeZip, String... arquivos) throws IOException {
        Path zipPath = Paths.get(AppConfig.OUTPUT_DIR, nomeZip);
        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipPath))) {
            for (String arquivoNome : arquivos) {
                Path arquivoPath = Paths.get(AppConfig.OUTPUT_DIR, arquivoNome);
                if (Files.exists(arquivoPath)) {
                    ZipEntry entry = new ZipEntry(arquivoNome);
                    zos.putNextEntry(entry);
                    Files.copy(arquivoPath, zos);
                    zos.closeEntry();
                }
            }
        }
    }

    public List<RegistroConsolidadoDTO> lerCsvConsolidado(Path path) throws Exception {
        try (Reader reader = Files.newBufferedReader(path)) {
            return new CsvToBeanBuilder<RegistroConsolidadoDTO>(reader)
                    .withType(RegistroConsolidadoDTO.class)
                    .withSeparator(',')
                    .withIgnoreLeadingWhiteSpace(true)
                    .withThrowExceptions(false)
                    .build()
                    .parse();
        }
    }

    public List<OperadoraCadastroDTO> lerCsvCadastro(Path path) throws Exception {
        try (Reader reader = Files.newBufferedReader(path)) {
            return new CsvToBeanBuilder<OperadoraCadastroDTO>(reader)
                    .withType(OperadoraCadastroDTO.class)
                    .withSeparator(';')
                    .withIgnoreLeadingWhiteSpace(true)
                    .build()
                    .parse();
        }
    }
}
