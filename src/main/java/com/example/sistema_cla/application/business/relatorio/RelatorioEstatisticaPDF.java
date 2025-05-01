package com.example.sistema_cla.application.business.relatorio;


import com.example.sistema_cla.domain.model.Relatorio;
import com.example.sistema_cla.domain.model.EstatisticaAcesso;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * Implementação concreta do Template Method para relatórios PDF
 */
public class RelatorioEstatisticaPDF extends RelatorioEstatisticaTemplate {

    @Override
    protected String getTipoRelatorio() {
        return "PDF";
    }

    @Override
    protected String construirCabecalho(String titulo, Map<String, Object> dados) {
        StringBuilder content = new StringBuilder();

        // Em uma implementação real, usaríamos iText ou outra biblioteca para gerar PDFs
        // Aqui, apenas simulamos o conteúdo do PDF como texto
        content.append("=================================================\n");
        content.append(titulo.toUpperCase()).append("\n");
        content.append("=================================================\n\n");

        LocalDate dataInicio = (LocalDate) dados.get("dataInicio");
        LocalDate dataFim = (LocalDate) dados.get("dataFim");
        String periodoFormatado = dataInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                " a " +
                dataFim.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        content.append("Período: ").append(periodoFormatado).append("\n");
        content.append("Data de geração: ")
                .append(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .append("\n\n");

        return content.toString();
    }

    @Override
    protected String construirCorpoEstatisticas(Map<String, Object> dados) {
        StringBuilder content = new StringBuilder();

        content.append("RESUMO GERAL DE ACESSOS\n");
        content.append("-------------------------------------------------\n\n");
        content.append("Total de acessos: ").append(dados.get("totalAcessos")).append("\n");
        content.append("Usuários ativos: ").append(dados.get("usuariosAtivos")).append("\n");
        content.append("Média de acessos por usuário: ").append(
                String.format("%.2f", (double) dados.get("mediaAcessosPorUsuario"))).append("\n");
        content.append("Tempo médio de sessão: ").append(
                formatarTempoEmMinutos((long) dados.get("tempoMedioSessaoMinutos"))).append("\n\n");

        content.append("ESTATÍSTICAS POR USUÁRIO\n");
        content.append("-------------------------------------------------\n\n");

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> estatisticasPorUsuario = (List<Map<String, Object>>) dados.get("estatisticasPorUsuario");

        if (estatisticasPorUsuario.isEmpty()) {
            content.append("Nenhuma estatística disponível para o período selecionado.\n\n");
        } else {
            // Cabeçalho da tabela
            content.append(String.format("%-20s %-10s %-20s %-15s %-15s\n",
                    "USUÁRIO", "ACESSOS", "ÚLTIMA ATIVIDADE", "TEMPO TOTAL", "PÁGINAS"));
            content.append("-------------------------------------------------\n");

            for (Map<String, Object> estatistica : estatisticasPorUsuario) {
                content.append(String.format("%-20s %-10d %-20s %-15s %-15d\n",
                        truncarTexto((String)estatistica.get("nomeUsuario"), 20),
                        ((Number)estatistica.get("quantidadeAcessos")).intValue(),
                        ((LocalDateTime)estatistica.get("ultimoAcesso")).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                        formatarTempoEmMinutos(((Number)estatistica.get("tempoTotalMinutos")).longValue()),
                        ((Number)estatistica.get("paginasVisitadas")).intValue()));
            }
            content.append("\n");
        }

        // Tendência de acessos
        content.append("TENDÊNCIA DE ACESSOS\n");
        content.append("-------------------------------------------------\n\n");
        content.append("Um gráfico de tendência seria exibido aqui no PDF real.\n\n");

        return content.toString();
    }

    @Override
    protected String construirRodape(Map<String, Object> dados) {
        StringBuilder content = new StringBuilder();

        content.append("-------------------------------------------------\n");
        content.append("Relatório gerado pelo Sistema CLA\n");
        content.append("© ").append(LocalDate.now().getYear()).append(" - Todos os direitos reservados\n");

        return content.toString();
    }

    @Override
    protected void aplicarFormatacaoEspecifica(Relatorio relatorio) {
        // Em uma implementação real, aplicaríamos configurações específicas do PDF
        // Como fontes, margens, etc. usando uma biblioteca como iText
        relatorio.setTipo("PDF");
    }

    private String formatarTempoEmMinutos(long minutos) {
        long horas = minutos / 60;
        long minutosRestantes = minutos % 60;

        if (horas > 0) {
            return horas + "h " + minutosRestantes + "min";
        } else {
            return minutosRestantes + "min";
        }
    }

    private String truncarTexto(String texto, int tamanhoMaximo) {
        if (texto == null || texto.length() <= tamanhoMaximo) {
            return texto;
        }
        return texto.substring(0, tamanhoMaximo - 3) + "...";
    }
}