package com.example.sistema_cla.infrastructure.exceptions;

public class EntityNotFoundException extends DAOException {
    public EntityNotFoundException(String entidade, Long id) {
        super(entidade + " com ID " + id + " não encontrada.");
    }
}