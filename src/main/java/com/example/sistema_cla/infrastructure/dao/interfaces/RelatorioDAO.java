package com.example.sistema_cla.infrastructure.dao.interfaces;

import com.example.sistema_cla.domain.model.Relatorio;
import com.example.sistema_cla.domain.model.Usuario;

import java.time.LocalDate;
import java.util.List;

public interface RelatorioDAO extends GenericDAO<Relatorio, Long> {
    List<Relatorio> findByUsuarioId(Long usuarioId);
    List<Relatorio> findByTipo(String tipo);
    List<Relatorio> findByCategoria(String categoria);
    List<Relatorio> findByUsuarioIdAndCategoria(Long usuarioId, String categoria);
    List<Relatorio> findByDataGeracaoBetween(LocalDate inicio, LocalDate fim);
    List<Relatorio> findByUsuario(Usuario usuario);
}