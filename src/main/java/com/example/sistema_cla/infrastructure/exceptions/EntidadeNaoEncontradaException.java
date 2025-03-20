package com.example.sistema_cla.infrastructure.exceptions;

public class EntidadeNaoEncontradaException extends RuntimeException {
    public EntidadeNaoEncontradaException(String entidade, Long id) {
        super(entidade + " com ID " + id + " não encontrada.");
    }
}