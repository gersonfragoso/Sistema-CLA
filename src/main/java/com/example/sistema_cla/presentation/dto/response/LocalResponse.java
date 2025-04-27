package com.example.sistema_cla.presentation.dto.response;

public record LocalResponse(
        Long id,
        String nome,
        String endereco,
        String tipoLocal,
        String statusAcessibilidade
) {}
