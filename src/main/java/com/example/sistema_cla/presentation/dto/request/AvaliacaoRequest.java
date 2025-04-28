package com.example.sistema_cla.presentation.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AvaliacaoRequest(
        @NotNull(message = "O ID do usuário é obrigatório")
        Long usuarioId,

        @NotNull(message = "O ID do local é obrigatório")
        Long localId,

        @Min(value = 1, message = "A nota deve ser pelo menos 1")
        @Max(value = 5, message = "A nota deve ser no máximo 5")
        int nota,

        @Size(max = 500, message = "O comentário deve ter no máximo 500 caracteres")
        String comentario
) {}