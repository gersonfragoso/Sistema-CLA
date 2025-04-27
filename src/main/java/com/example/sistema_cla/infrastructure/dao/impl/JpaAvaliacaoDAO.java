package com.example.sistema_cla.infrastructure.dao.impl;

import com.example.sistema_cla.domain.model.Avaliacao;
import com.example.sistema_cla.domain.model.Local;
import com.example.sistema_cla.infrastructure.dao.interfaces.AvaliacaoDAO;
import com.example.sistema_cla.infrastructure.exceptions.ConnectionException;
import jakarta.persistence.PersistenceException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JpaAvaliacaoDAO extends JpaGenericDAO<Avaliacao, Long> implements AvaliacaoDAO {

    public JpaAvaliacaoDAO() {
        super(Avaliacao.class);
    }

    @Override
    public List<Avaliacao> findByUsuarioId(Long usuarioId) {
        try {
            return entityManager
                    .createQuery("SELECT a FROM Avaliacao a WHERE a.usuario.id = :usuarioId", Avaliacao.class)
                    .setParameter("usuarioId", usuarioId)
                    .getResultList();
        } catch (PersistenceException e) {
            throw new ConnectionException("Erro de conexão ao buscar avaliações por usuário ID: " + usuarioId, e);
        }
    }

    @Override
    public List<Avaliacao> findByLocalId(Long localId) {
        try {
            return entityManager
                    .createQuery("SELECT a FROM Avaliacao a WHERE a.localAcessivel.id = :localId", Avaliacao.class)
                    .setParameter("localId", localId)
                    .getResultList();
        } catch (PersistenceException e) {
            throw new ConnectionException("Erro de conexão ao buscar avaliações por local ID: " + localId, e);
        }
    }

    @Override
    public List<Avaliacao> findByLocalAcessivel(Local local) {
        try {
            return entityManager
                    .createQuery("SELECT a FROM Avaliacao a WHERE a.localAcessivel = :local", Avaliacao.class)
                    .setParameter("local", local)
                    .getResultList();
        } catch (PersistenceException e) {
            throw new ConnectionException("Erro de conexão ao buscar avaliações por objeto local", e);
        }
    }
}