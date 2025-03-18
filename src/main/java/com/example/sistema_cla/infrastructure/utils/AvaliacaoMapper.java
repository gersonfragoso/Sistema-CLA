package com.example.sistema_cla.infrastructure.utils;

import com.example.sistema_cla.domain.model.Avaliacao;
import com.example.sistema_cla.domain.model.Local;
import com.example.sistema_cla.domain.model.Usuario;
import com.example.sistema_cla.presentation.dto.request.AvaliacaoRequest;
import com.example.sistema_cla.presentation.dto.response.AvaliacaoResponse;

import java.time.LocalDate;

public class AvaliacaoMapper {
    public static AvaliacaoResponse toResponse(Avaliacao avaliacao) {
        return new AvaliacaoResponse(
                avaliacao.getId(),
                avaliacao.getUsuario().getId(),
                avaliacao.getLocalAcessivel().getId(),
                avaliacao.getNota(),
                avaliacao.getComentario()
        );
    }

    public static Avaliacao toEntity(AvaliacaoRequest request, Usuario usuario, Local local) {
        return new Avaliacao(null, usuario, local, request.nota(), request.comentario(), LocalDate.now());
    }
}