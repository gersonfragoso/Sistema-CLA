package com.example.sistema_cla.domain.repository;

public class EntidadeNaoEncontradaException extends RuntimeException {
    public EntidadeNaoEncontradaException(String entidade, Long id) {
        super(entidade + " com ID " + id + " não encontrada.");
    }
}