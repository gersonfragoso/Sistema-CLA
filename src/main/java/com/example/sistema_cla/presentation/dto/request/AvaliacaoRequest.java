package com.example.sistema_cla.presentation.dto.request;

public record AvaliacaoRequest(
        Long usuarioId,
        Long localId,
        int nota,
        String comentario
) {}
