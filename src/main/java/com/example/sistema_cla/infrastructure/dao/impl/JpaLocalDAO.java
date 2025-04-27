package com.example.sistema_cla.infrastructure.dao.impl;

import com.example.sistema_cla.domain.enums.StatusAcessibilidade;
import com.example.sistema_cla.domain.model.Local;
import com.example.sistema_cla.infrastructure.dao.interfaces.LocalDAO;
import com.example.sistema_cla.infrastructure.exceptions.ConnectionException;
import com.example.sistema_cla.infrastructure.exceptions.InvalidStatusAcessibilidadeException;
import jakarta.persistence.PersistenceException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JpaLocalDAO extends JpaGenericDAO<Local, Long> implements LocalDAO {

    public JpaLocalDAO() {
        super(Local.class);
    }

    @Override
    public List<Local> findByTipoLocal(String tipoLocal) {
        try {
            return entityManager
                    .createQuery("SELECT l FROM Local l WHERE l.tipoLocal = :tipoLocal", Local.class)
                    .setParameter("tipoLocal", tipoLocal)
                    .getResultList();
        } catch (PersistenceException e) {
            throw new ConnectionException("Erro de conexão ao buscar locais por tipo: " + tipoLocal, e);
        }
    }

    @Override
    public List<Local> findByStatusAcessibilidade(StatusAcessibilidade status) {
        if (status == null) {
            throw new InvalidStatusAcessibilidadeException(null);
        }

        try {
            return entityManager
                    .createQuery("SELECT l FROM Local l WHERE l.statusAcessibilidade = :status", Local.class)
                    .setParameter("status", status)
                    .getResultList();
        } catch (PersistenceException e) {
            throw new ConnectionException("Erro de conexão ao buscar locais por status: " + status, e);
        }
    }
}