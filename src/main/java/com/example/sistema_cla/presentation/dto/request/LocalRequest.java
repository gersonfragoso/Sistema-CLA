package com.example.sistema_cla.presentation.dto.request;

import com.example.sistema_cla.domain.enums.StatusAcessibilidade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LocalRequest(
        @NotBlank(message = "O nome do local é obrigatório")
        String nome,

        @NotBlank(message = "O endereço do local é obrigatório")
        String endereco,

        @NotBlank(message = "O tipo do local é obrigatório")
        String tipoLocal,

        @NotNull(message = "O status de acessibilidade é obrigatório")
        StatusAcessibilidade statusAcessibilidade
) {}