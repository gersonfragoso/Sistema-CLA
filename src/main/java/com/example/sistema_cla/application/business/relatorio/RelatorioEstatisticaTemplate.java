package com.example.sistema_cla.application.business.relatorio;

import com.example.sistema_cla.domain.model.Relatorio;
import com.example.sistema_cla.domain.model.Usuario;

import java.time.LocalDate;
import java.util.Map;

/**
 * Implementação do padrão Template Method para geração de relatórios
 * de estatísticas de acesso dos usuários no sistema.
 */
public abstract class RelatorioEstatisticaTemplate {

    /**
     * Método template que define o algoritmo para geração de relatórios
     */
    public final Relatorio gerarRelatorio(String titulo, Map<String, Object> estatisticas, Usuario solicitante) {
        // 1. Criar objeto relatório básico
        Relatorio relatorio = new Relatorio();
        relatorio.setTitulo(titulo);
        relatorio.setTipo(getTipoRelatorio());
        relatorio.setDataGeracao(LocalDate.now());
        relatorio.setUsuario(solicitante);

        // 2. Coletar dados adicionais se necessário
        Map<String, Object> dadosProcessados = processarDadosEstatisticos(estatisticas);

        // 3. Construir o cabeçalho do relatório
        String conteudo = construirCabecalho(titulo, dadosProcessados);

        // 4. Construir o corpo do relatório com estatísticas
        conteudo += construirCorpoEstatisticas(dadosProcessados);

        // 5. Construir o rodapé do relatório
        conteudo += construirRodape(dadosProcessados);

        // 6. Aplicar estilos e formatação específica do formato
        relatorio.setConteudo(conteudo);
        aplicarFormatacaoEspecifica(relatorio);

        return relatorio;
    }

    /**
     * @return O tipo de relatório ("HTML", "PDF", etc.)
     */
    protected abstract String getTipoRelatorio();

    /**
     * Permite processar ou transformar os dados de estatísticas antes da formatação
     */
    protected Map<String, Object> processarDadosEstatisticos(Map<String, Object> estatisticas) {
        // Implementação padrão retorna os dados sem modificações
        return estatisticas;
    }

    /**
     * Constrói o cabeçalho do relatório
     */
    protected abstract String construirCabecalho(String titulo, Map<String, Object> dados);

    /**
     * Constrói o corpo do relatório com as estatísticas de acesso
     */
    protected abstract String construirCorpoEstatisticas(Map<String, Object> dados);

    /**
     * Constrói o rodapé do relatório
     */
    protected abstract String construirRodape(Map<String, Object> dados);

    /**
     * Aplica formatação específica do formato do relatório
     */
    protected abstract void aplicarFormatacaoEspecifica(Relatorio relatorio);
}