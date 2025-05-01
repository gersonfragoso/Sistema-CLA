package com.example.sistema_cla.domain.model;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class AvaliacaoMemento {
    private final int nota;
    private final String comentario;
    private final LocalDate dataAvaliacao;
    private final Long id;
    private final Long usuarioId;
    private final Long localId;

    public AvaliacaoMemento(Avaliacao avaliacao) {
        this.nota = avaliacao.getNota();
        this.comentario = avaliacao.getComentario();
        this.dataAvaliacao = avaliacao.getDataAvaliacao();
        this.id = avaliacao.getId();
        this.usuarioId = avaliacao.getUsuario() != null ? avaliacao.getUsuario().getId() : null;
        this.localId = avaliacao.getLocalAcessivel() != null ? avaliacao.getLocalAcessivel().getId() : null;
    }
}