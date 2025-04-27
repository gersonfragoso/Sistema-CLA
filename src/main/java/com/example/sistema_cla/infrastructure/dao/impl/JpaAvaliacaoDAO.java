package com.example.sistema_cla.infrastructure.dao.impl;

import com.example.sistema_cla.domain.model.Avaliacao;
import com.example.sistema_cla.infrastructure.dao.interfaces.AvaliacaoDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JpaAvaliacaoDAO extends JpaGenericDAO<Avaliacao, Long> implements AvaliacaoDAO {

    public JpaAvaliacaoDAO() {
        super(Avaliacao.class);
    }

    @Override
    public List<Avaliacao> findByUsuarioId(Long usuarioId) {
        return entityManager
                .createQuery("SELECT a FROM Avaliacao a WHERE a.usuario.id = :usuarioId", Avaliacao.class)
                .setParameter("usuarioId", usuarioId)
                .getResultList();
    }

    @Override
    public List<Avaliacao> findByLocalId(Long localId) {
        return entityManager
                .createQuery("SELECT a FROM Avaliacao a WHERE a.localAcessivel.id = :localId", Avaliacao.class)
                .setParameter("localId", localId)
                .getResultList();
    }
}