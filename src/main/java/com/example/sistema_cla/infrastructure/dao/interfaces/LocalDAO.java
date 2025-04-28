package com.example.sistema_cla.infrastructure.dao.interfaces;

import com.example.sistema_cla.domain.enums.StatusAcessibilidade;
import com.example.sistema_cla.domain.model.Local;

import java.util.List;

public interface LocalDAO extends GenericDAO<Local, Long> {
    List<Local> findByTipoLocal(String tipoLocal);
    List<Local> findByStatusAcessibilidade(StatusAcessibilidade status);
}