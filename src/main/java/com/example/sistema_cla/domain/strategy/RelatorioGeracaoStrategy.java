package com.example.sistema_cla.domain.strategy;


import com.example.sistema_cla.domain.model.EstatisticaAcesso;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface RelatorioGeracaoStrategy {
    /**
     * Gera o conteúdo de um relatório
     */
    String gerarConteudo(
            String titulo,
            LocalDate dataInicio,
            LocalDate dataFim,
            List<EstatisticaAcesso> estatisticasAcesso,
            Map<String, Object> resumoGeral
    );

    /**
     * Retorna o tipo de relatório que esta estratégia gera
     */
    String getTipo();
}