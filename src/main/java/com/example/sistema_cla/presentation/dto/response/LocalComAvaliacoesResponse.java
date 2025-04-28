package com.example.sistema_cla.presentation.dto.response;

import java.util.List;

public record LocalComAvaliacoesResponse(
        Long id,
        String nome,
        String endereco,
        String tipoAcessibilidade,
        double mediaAvaliacoes,
        List<AvaliacaoResponse> avaliacoes
) {}
