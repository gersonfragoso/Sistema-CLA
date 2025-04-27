package com.example.sistema_cla.infrastructure.exceptions;

public class DataIntegrityException extends DAOException {
    public DataIntegrityException(String message) {
        super(message);
    }

    public DataIntegrityException(String message, Throwable cause) {
        super(message, cause);
    }
}