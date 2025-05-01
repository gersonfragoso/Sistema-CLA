package com.example.sistema_cla.presentation.dto.response;

public record TelefoneResponse(
        int ddd,
        String numeroTelefone,
        String telefoneFormatado
) {}