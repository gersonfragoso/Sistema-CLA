package com.example.sistema_cla.domain.strategy.impl;


import com.example.sistema_cla.domain.model.EstatisticaAcesso;
import com.example.sistema_cla.domain.strategy.RelatorioGeracaoStrategy;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Component
public class HtmlRelatorioStrategy implements RelatorioGeracaoStrategy {

    @Override
    public String gerarConteudo(
            String titulo,
            LocalDate dataInicio,
            LocalDate dataFim,
            List<EstatisticaAcesso> estatisticasAcesso,
            Map<String, Object> resumoGeral) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head>");
        html.append("<meta charset=\"UTF-8\">");
        html.append("<title>").append(titulo).append("</title>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; margin: 20px; }");
        html.append("h1, h2 { color: #333; }");
        html.append("table { border-collapse: collapse; width: 100%; }");
        html.append("table, th, td { border: 1px solid #ddd; }");
        html.append("th, td { padding: 8px; text-align: left; }");
        html.append("th { background-color: #f2f2f2; }");
        html.append("tr:nth-child(even) { background-color: #f9f9f9; }");
        html.append("</style>");
        html.append("</head><body>");

        // Cabeçalho
        html.append("<h1>").append(titulo).append("</h1>");
        html.append("<p><strong>Período:</strong> ")
                .append(dataInicio.format(formatter))
                .append(" a ")
                .append(dataFim.format(formatter))
                .append("</p>");

        // Resumo geral
        html.append("<h2>Resumo Geral</h2>");
        html.append("<ul>");
        html.append("<li><strong>Total de Acessos:</strong> ").append(resumoGeral.get("totalAcessos")).append("</li>");
        html.append("<li><strong>Usuários Ativos:</strong> ").append(resumoGeral.get("usuariosAtivos")).append("</li>");
        html.append("<li><strong>Tempo Médio de Sessão:</strong> ").append(resumoGeral.get("tempoMedioSessaoMinutos")).append(" minutos</li>");
        html.append("<li><strong>Média de Acessos por Usuário:</strong> ").append(String.format("%.1f", resumoGeral.get("mediaAcessosPorUsuario"))).append("</li>");
        html.append("</ul>");

        // Estatísticas por usuário
        html.append("<h2>Estatísticas por Usuário</h2>");
        html.append("<table>");
        html.append("<tr><th>Usuário</th><th>Acessos</th><th>Último Acesso</th><th>Tempo Total (min)</th><th>Páginas Visitadas</th></tr>");

        for (EstatisticaAcesso estatistica : estatisticasAcesso) {
            html.append("<tr>");
            html.append("<td>").append(estatistica.getNomeUsuario()).append("</td>");
            html.append("<td>").append(estatistica.getQuantidadeAcessos()).append("</td>");
            html.append("<td>").append(estatistica.getUltimoAcesso().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("</td>");
            html.append("<td>").append(estatistica.getTempoTotalMinutos()).append("</td>");
            html.append("<td>").append(estatistica.getPaginasVisitadas()).append("</td>");
            html.append("</tr>");
        }

        html.append("</table>");
        html.append("</body></html>");

        return html.toString();
    }

    @Override
    public String getTipo() {
        return "HTML";
    }
}