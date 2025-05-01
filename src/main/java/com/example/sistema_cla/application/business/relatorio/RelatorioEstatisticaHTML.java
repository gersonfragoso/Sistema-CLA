package com.example.sistema_cla.application.business.relatorio;


import com.example.sistema_cla.domain.model.Relatorio;
import com.example.sistema_cla.domain.model.EstatisticaAcesso;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * Implementação concreta do Template Method para relatórios HTML
 */
public class RelatorioEstatisticaHTML extends RelatorioEstatisticaTemplate {

    @Override
    protected String getTipoRelatorio() {
        return "HTML";
    }

    @Override
    protected String construirCabecalho(String titulo, Map<String, Object> dados) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n<html>\n<head>\n");
        html.append("<title>").append(titulo).append("</title>\n");
        html.append("<meta charset=\"UTF-8\">\n");
        html.append("</head>\n<body>\n");

        html.append("<header>\n");
        html.append("<h1>").append(titulo).append("</h1>\n");

        LocalDate dataInicio = (LocalDate) dados.get("dataInicio");
        LocalDate dataFim = (LocalDate) dados.get("dataFim");
        String periodoFormatado = dataInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                " a " +
                dataFim.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        html.append("<p>Período: ").append(periodoFormatado).append("</p>\n");
        html.append("<p>Data de geração: ").append(
                        LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .append("</p>\n");
        html.append("</header>\n");

        return html.toString();
    }

    @Override
    protected String construirCorpoEstatisticas(Map<String, Object> dados) {
        StringBuilder html = new StringBuilder();

        // Resumo geral
        html.append("<section>\n");
        html.append("<h2>Resumo Geral de Acessos</h2>\n");
        html.append("<div class=\"resumo\">\n");
        html.append("<p><strong>Total de acessos:</strong> ").append(dados.get("totalAcessos")).append("</p>\n");
        html.append("<p><strong>Usuários ativos:</strong> ").append(dados.get("usuariosAtivos")).append("</p>\n");
        html.append("<p><strong>Média de acessos por usuário:</strong> ").append(
                String.format("%.2f", (double) dados.get("mediaAcessosPorUsuario"))).append("</p>\n");
        html.append("<p><strong>Tempo médio de sessão:</strong> ").append(
                formatarTempoEmMinutos((long) dados.get("tempoMedioSessaoMinutos"))).append("</p>\n");
        html.append("</div>\n");
        html.append("</section>\n");

        // Estatísticas por usuário
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> estatisticasPorUsuario = (List<Map<String, Object>>) dados.get("estatisticasPorUsuario");
        html.append("<section>\n");
        html.append("<h2>Estatísticas por Usuário</h2>\n");

        if (estatisticasPorUsuario.isEmpty()) {
            html.append("<p>Nenhuma estatística disponível para o período selecionado.</p>\n");
        } else {
            html.append("<table>\n");
            html.append("<thead>\n");
            html.append("<tr>\n");
            html.append("<th>Usuário</th>\n");
            html.append("<th>Acessos</th>\n");
            html.append("<th>Última Atividade</th>\n");
            html.append("<th>Tempo Total</th>\n");
            html.append("<th>Páginas Visitadas</th>\n");
            html.append("</tr>\n");
            html.append("</thead>\n");
            html.append("<tbody>\n");

            for (Map<String, Object> estatistica : estatisticasPorUsuario) {
                html.append("<tr>\n");
                html.append("<td>").append(estatistica.get("nomeUsuario")).append("</td>\n");
                html.append("<td>").append(estatistica.get("quantidadeAcessos")).append("</td>\n");
                html.append("<td>").append(
                                ((LocalDateTime)estatistica.get("ultimoAcesso")).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                        .append("</td>\n");
                html.append("<td>").append(formatarTempoEmMinutos(((Number)estatistica.get("tempoTotalMinutos")).longValue())).append("</td>\n");
                html.append("<td>").append(estatistica.get("paginasVisitadas")).append("</td>\n");
                html.append("</tr>\n");
            }

            html.append("</tbody>\n");
            html.append("</table>\n");
        }

        html.append("</section>\n");

        // Gráfico de tendência (texto representando um gráfico)
        html.append("<section>\n");
        html.append("<h2>Tendência de Acessos</h2>\n");
        html.append("<div class=\"grafico\">\n");
        html.append("<p>Gráfico de tendência seria exibido aqui.</p>\n");
        html.append("</div>\n");
        html.append("</section>\n");

        return html.toString();
    }

    @Override
    protected String construirRodape(Map<String, Object> dados) {
        StringBuilder html = new StringBuilder();

        html.append("<footer>\n");
        html.append("<p>Relatório gerado pelo Sistema CLA</p>\n");
        html.append("<p>© ").append(LocalDate.now().getYear()).append(" - Todos os direitos reservados</p>\n");
        html.append("</footer>\n");

        html.append("</body>\n</html>");

        return html.toString();
    }

    @Override
    protected void aplicarFormatacaoEspecifica(Relatorio relatorio) {
        String css = "<style>\n" +
                "body { font-family: Arial, sans-serif; margin: 0; padding: 20px; color: #333; }\n" +
                "header { margin-bottom: 30px; border-bottom: 1px solid #ddd; padding-bottom: 10px; }\n" +
                "h1 { color: #2c3e50; }\n" +
                "h2 { color: #3498db; margin-top: 20px; }\n" +
                "section { margin-bottom: 30px; }\n" +
                "table { width: 100%; border-collapse: collapse; margin-top: 15px; }\n" +
                "th, td { padding: 10px; text-align: left; border: 1px solid #ddd; }\n" +
                "th { background-color: #f2f2f2; }\n" +
                ".resumo { background-color: #f9f9f9; padding: 15px; border-radius: 5px; }\n" +
                ".grafico { background-color: #f9f9f9; padding: 15px; border-radius: 5px; text-align: center; }\n" +
                "footer { margin-top: 30px; border-top: 1px solid #ddd; padding-top: 10px; text-align: center; font-size: 12px; color: #777; }\n" +
                "</style>\n";

        String conteudo = relatorio.getConteudo();
        conteudo = conteudo.replace("</head>", css + "</head>");
        relatorio.setConteudo(conteudo);
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
}