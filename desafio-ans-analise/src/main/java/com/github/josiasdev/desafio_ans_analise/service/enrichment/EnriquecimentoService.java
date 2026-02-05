package com.github.josiasdev.desafio_ans_analise.service.enrichment;

import com.github.josiasdev.desafio_ans_analise.dto.OperadoraCadastroDTO;
import com.github.josiasdev.desafio_ans_analise.dto.RegistroConsolidadoDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EnriquecimentoService {
    public List<RegistroConsolidadoDTO> enriquecer(List<RegistroConsolidadoDTO> despesas, List<OperadoraCadastroDTO> cadastro) {

        Map<String, OperadoraCadastroDTO> mapaCadastro = cadastro.stream()
                .collect(Collectors.toMap(
                        op -> padronizarRegistroAns(op.getRegistroAns()),
                        operadora -> operadora,
                        (existente, novo) -> existente
                ));

        return despesas.stream().map(despesa -> {
            String registroBusca = padronizarRegistroAns(despesa.getCnpj());

            OperadoraCadastroDTO info = mapaCadastro.get(registroBusca);

            if (info != null) {
                despesa.setRegistroAns(info.getRegistroAns());
                despesa.setModalidade(info.getModalidade());
                despesa.setUf(info.getUf());
                despesa.setRazaoSocial(info.getRazaoSocial());
            } else {
                despesa.setUf("NAO_LOCALIZADO");
            }
            return despesa;
        }).collect(Collectors.toList());
    }


    private String padronizarRegistroAns(String registro) {
        if (registro == null) return "";

        String limpo = registro.replace("\"", "").trim().replaceAll("\\D", "");

        if (limpo.isEmpty()) return "";

        return String.format("%06d", Integer.parseInt(limpo));
    }
}