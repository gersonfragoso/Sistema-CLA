package com.example.sistema_cla.presentation.dto.response;

import java.time.LocalDate;

public record RelatorioResponse(
        Long id,
        String titulo,
        LocalDate dataGeracao,
        String tipo,
        String categoria,
        String periodo,
        String conteudo,
        Long usuarioId
) {}