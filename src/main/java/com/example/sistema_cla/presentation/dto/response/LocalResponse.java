package com.example.sistema_cla.presentation.dto.response;

public record LocalResponse(
        String nome,
        String endereco,
        String tipoLocal,
        String statusAcessibilidade
) {}
