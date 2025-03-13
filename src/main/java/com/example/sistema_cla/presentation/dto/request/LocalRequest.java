package com.example.sistema_cla.presentation.dto.request;

import com.example.sistema_cla.domain.enums.StatusAcessibilidade;

public record LocalRequest(
        String nome,
        String endereco,
        String tipoLocal,
        StatusAcessibilidade statusAcessibilidade
) {}