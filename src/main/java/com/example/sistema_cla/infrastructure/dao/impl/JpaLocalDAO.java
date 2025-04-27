package com.example.sistema_cla.infrastructure.dao.impl;

import com.example.sistema_cla.domain.enums.StatusAcessibilidade;
import com.example.sistema_cla.domain.model.Local;
import com.example.sistema_cla.infrastructure.dao.interfaces.LocalDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JpaLocalDAO extends JpaGenericDAO<Local, Long> implements LocalDAO {

    public JpaLocalDAO() {
        super(Local.class);
    }

    @Override
    public List<Local> findByTipoLocal(String tipoLocal) {
        return entityManager
                .createQuery("SELECT l FROM Local l WHERE l.tipoLocal = :tipoLocal", Local.class)
                .setParameter("tipoLocal", tipoLocal)
                .getResultList();
    }

    @Override
    public List<Local> findByStatusAcessibilidade(StatusAcessibilidade status) {
        return entityManager
                .createQuery("SELECT l FROM Local l WHERE l.statusAcessibilidade = :status", Local.class)
                .setParameter("status", status)
                .getResultList();
    }
}