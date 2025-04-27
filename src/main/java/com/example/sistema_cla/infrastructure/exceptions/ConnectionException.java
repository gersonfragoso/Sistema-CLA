package com.example.sistema_cla.infrastructure.exceptions;

public class ConnectionException extends DAOException {
    public ConnectionException(String message) {
        super(message);
    }

    public ConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}