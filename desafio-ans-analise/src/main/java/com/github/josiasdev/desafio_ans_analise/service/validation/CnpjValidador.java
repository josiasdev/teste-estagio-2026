package com.github.josiasdev.desafio_ans_analise.service.validation;

import com.github.josiasdev.desafio_ans_analise.dto.RegistroConsolidadoDTO;
import com.github.josiasdev.desafio_ans_analise.util.ValidadorDocumento;
import org.springframework.stereotype.Component;

@Component
public class CnpjValidador implements ValidadorRegistro {
    @Override
    public boolean isValid(RegistroConsolidadoDTO r) {
        return ValidadorDocumento.isCnpjValido(r.getCnpj());
    }
}