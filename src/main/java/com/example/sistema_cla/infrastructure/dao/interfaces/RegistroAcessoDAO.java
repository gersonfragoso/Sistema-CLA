package com.example.sistema_cla.infrastructure.dao.interfaces;

import com.example.sistema_cla.domain.model.EstatisticaAcesso;
import com.example.sistema_cla.domain.model.RegistroAcesso;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface RegistroAcessoDAO extends GenericDAO<RegistroAcesso, Long> {
    List<RegistroAcesso> findByUsuarioId(Long usuarioId);
    List<RegistroAcesso> findByDataAcessoBetween(LocalDateTime inicio, LocalDateTime fim);
    List<RegistroAcesso> findByUsuarioIdAndDataAcessoBetween(Long usuarioId, LocalDateTime inicio, LocalDateTime fim);

    /**
     * Obter estatísticas de acesso para todos os usuários em um período
     */
    List<EstatisticaAcesso> getEstatisticasAcessoPorPeriodo(LocalDate inicio, LocalDate fim);

    /**
     * Obter estatísticas de acesso para um usuário específico em um período
     */
    EstatisticaAcesso getEstatisticaAcessoUsuario(Long usuarioId, LocalDate inicio, LocalDate fim);

    /**
     * Obter resumo geral de acessos no período
     */
    Map<String, Object> getResumoGeralAcessos(LocalDate inicio, LocalDate fim);
}