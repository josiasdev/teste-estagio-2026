package com.github.josiasdev.desafio_ans_analise.service.aggregation;

import com.github.josiasdev.desafio_ans_analise.dto.DespesaAgregadaDTO;
import com.github.josiasdev.desafio_ans_analise.dto.RegistroConsolidadoDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AgregacaoService {

    public List<DespesaAgregadaDTO> agruparECalcularEstatisticas(List<RegistroConsolidadoDTO> lista) {
        Map<String, List<RegistroConsolidadoDTO>> agrupado = lista.stream()
                .collect(Collectors.groupingBy(d -> d.getRazaoSocial() + "|" + d.getUf()));

        return agrupado.entrySet().stream().map(entry -> {
                    String[] keys = entry.getKey().split("\\|");
                    List<RegistroConsolidadoDTO> registros = entry.getValue();

                    BigDecimal total = registros.stream()
                            .map(RegistroConsolidadoDTO::getValorDespesa)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    BigDecimal media = total.divide(BigDecimal.valueOf(registros.size()), 2, RoundingMode.HALF_UP);

                    BigDecimal variancia = calcularVariancia(registros, media);
                    BigDecimal desvioPadrao = BigDecimal.valueOf(Math.sqrt(variancia.doubleValue()))
                            .setScale(2, RoundingMode.HALF_UP);

                    return DespesaAgregadaDTO.builder()
                            .razaoSocial(keys[0])
                            .uf(keys[1])
                            .valorTotal(total.setScale(2, RoundingMode.HALF_UP))
                            .mediaTrimestral(media)
                            .desvioPadrao(desvioPadrao)
                            .build();
                })
                .sorted(Comparator.comparing(DespesaAgregadaDTO::getValorTotal).reversed())
                .collect(Collectors.toList());
    }

    private BigDecimal calcularVariancia(List<RegistroConsolidadoDTO> registros, BigDecimal media) {
        if (registros.size() <= 1) return BigDecimal.ZERO;

        BigDecimal somaQuadrados = registros.stream()
                .map(r -> r.getValorDespesa().subtract(media).pow(2))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return somaQuadrados.divide(BigDecimal.valueOf(registros.size() - 1), 2, RoundingMode.HALF_UP);
    }
}