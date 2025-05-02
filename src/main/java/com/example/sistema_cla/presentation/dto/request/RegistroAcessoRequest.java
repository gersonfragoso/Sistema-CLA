package com.example.sistema_cla.presentation.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegistroAcessoRequest(
        @NotNull(message = "O ID do usuário é obrigatório")
        Long usuarioId,

        @NotBlank(message = "O IP de acesso é obrigatório")
        String ipAcesso,

        @NotBlank(message = "O dispositivo é obrigatório")
        String dispositivo,

        @Min(value = 0, message = "O tempo de sessão não pode ser negativo")
        int tempoSessaoMinutos,

        @Min(value = 0, message = "O número de páginas visitadas não pode ser negativo")
        int paginasVisitadas
) {}