package com.example.sistema_cla.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RelatorioEstatisticaRequest(
        @NotBlank(message = "O título é obrigatório")
        String titulo,

        @NotBlank(message = "O formato é obrigatório")
        String formato,

        @NotNull(message = "A data de início é obrigatória")
        LocalDate dataInicio,

        @NotNull(message = "A data de fim é obrigatória")
        LocalDate dataFim,

        @NotNull(message = "O ID do usuário é obrigatório")
        Long usuarioId
) {}