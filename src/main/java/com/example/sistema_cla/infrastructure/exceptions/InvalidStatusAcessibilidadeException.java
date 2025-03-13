package com.example.sistema_cla.infrastructure.exceptions;

import com.example.sistema_cla.domain.enums.StatusAcessibilidade;

public class InvalidStatusAcessibilidadeException extends RuntimeException {
    public InvalidStatusAcessibilidadeException(StatusAcessibilidade status) {
        super("Status de acessibilidade inválido: " + status);
    }
}
