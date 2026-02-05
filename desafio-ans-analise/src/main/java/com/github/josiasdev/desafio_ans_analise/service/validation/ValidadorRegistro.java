package com.github.josiasdev.desafio_ans_analise.service.validation;
import com.github.josiasdev.desafio_ans_analise.dto.RegistroConsolidadoDTO;

public interface ValidadorRegistro {
    boolean isValid(RegistroConsolidadoDTO registro);
}