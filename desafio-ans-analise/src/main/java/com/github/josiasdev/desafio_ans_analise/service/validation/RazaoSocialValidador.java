package com.github.josiasdev.desafio_ans_analise.service.validation;

import com.github.josiasdev.desafio_ans_analise.dto.RegistroConsolidadoDTO;
import org.springframework.stereotype.Component;

@Component
public class RazaoSocialValidador implements ValidadorRegistro {
    @Override
    public boolean isValid(RegistroConsolidadoDTO r) {
        return r.getRazaoSocial() != null && !r.getRazaoSocial().trim().isEmpty();
    }
}