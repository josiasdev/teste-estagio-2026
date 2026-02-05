package com.github.josiasdev.desafioans.model;

import com.github.josiasdev.desafioans.model.DespesaConsolidada;
import com.github.josiasdev.desafioans.util.ZipUtils;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConsolidationService {

    public void consolidar(List<DespesaConsolidada> lista) throws Exception {
        String csvPath = "temp/ans/consolidado.csv";

        // Tratamento de Inconsistência: CNPJs duplicados com razões diferentes
        // Abordagem: Manter a primeira Razão Social encontrada para o mesmo CNPJ para normalizar o arquivo
        Map<String, String> cnpjMap = new HashMap<>();
        for (DespesaConsolidada d : lista) {
            if (!cnpjMap.containsKey(d.getCnpj())) {
                cnpjMap.put(d.getCnpj(), d.getRazaoSocial());
            } else {
                d.setRazaoSocial(cnpjMap.get(d.getCnpj())); // Normaliza a razão
            }
        }

        // Escrever CSV
        try (Writer writer = new FileWriter(csvPath)) {
            StatefulBeanToCsv<DespesaConsolidada> beanToCsv = new StatefulBeanToCsvBuilder<DespesaConsolidada>(writer)
                    .withSeparator(',')
                    .build();
            beanToCsv.write(lista);
        }

        // Compactar para ZIP final
        ZipUtils.compactarParaZip(csvPath, "consolidado_despesas.zip");

        // Limpeza opcional (Remover CSV temporário)
        Files.deleteIfExists(Paths.get(csvPath));
    }
}