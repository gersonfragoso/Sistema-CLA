package com.example.sistema_cla.presentation.dto.response;

public record AvaliacaoResponse(
        Long id,
        Long usuarioId,
        Long localId,
        int nota,
        String comentario
) {}

