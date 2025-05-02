package com.example.sistema_cla.infrastructure.dao.interfaces;

import com.example.sistema_cla.domain.model.EstatisticaAcesso;
import java.util.Optional;
import java.util.List;

public interface EstatisticaAcessoDAO extends GenericDAO<EstatisticaAcesso, Long> {
    /**
     * Busca estatística de acesso por ID do usuário
     * @param usuarioId ID do usuário
     * @return Optional contendo EstatisticaAcesso se encontrada
     */
    Optional<EstatisticaAcesso> findByUsuarioId(Long usuarioId);

    /**
     * Atualiza a estatística de acesso para um usuário
     * @param estatisticaAcesso Estatística a ser atualizada
     * @return Estatística atualizada
     */
    EstatisticaAcesso update(EstatisticaAcesso estatisticaAcesso);

    /**
     * Busca as estatísticas dos usuários mais ativos
     * @param limit Número máximo de resultados
     * @return Lista de estatísticas ordenadas por quantidade de acessos
     */
    List<EstatisticaAcesso> findTopUsuariosAtivos(int limit);
}