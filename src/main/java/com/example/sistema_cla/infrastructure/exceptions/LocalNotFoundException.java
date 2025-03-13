package com.example.sistema_cla.infrastructure.exceptions;

public class LocalNotFoundException extends RuntimeException {
    public LocalNotFoundException(Long id) {
        super("Local com ID " + id + " não encontrado");
    }
}
