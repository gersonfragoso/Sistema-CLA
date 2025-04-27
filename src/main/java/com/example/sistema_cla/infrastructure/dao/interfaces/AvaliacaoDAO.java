package com.example.sistema_cla.infrastructure.dao.interfaces;

import com.example.sistema_cla.domain.model.Avaliacao;

import java.util.List;

public interface AvaliacaoDAO extends GenericDAO<Avaliacao, Long> {
    List<Avaliacao> findByUsuarioId(Long usuarioId);
    List<Avaliacao> findByLocalId(Long localId);
}
