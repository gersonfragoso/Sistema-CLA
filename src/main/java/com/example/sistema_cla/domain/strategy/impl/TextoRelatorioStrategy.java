package com.example.sistema_cla.domain.strategy.impl;

import com.example.sistema_cla.domain.model.EstatisticaAcesso;
import com.example.sistema_cla.domain.strategy.RelatorioGeracaoStrategy;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Component
public class TextoRelatorioStrategy implements RelatorioGeracaoStrategy {

    @Override
    public String gerarConteudo(
            String titulo,
            LocalDate dataInicio,
            LocalDate dataFim,
            List<EstatisticaAcesso> estatisticasAcesso,
            Map<String, Object> resumoGeral) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        StringBuilder texto = new StringBuilder();

        // Cabeçalho
        texto.append(titulo).append("\n");
        texto.append("=".repeat(titulo.length())).append("\n\n");
        texto.append("Período: ")
                .append(dataInicio.format(formatter))
                .append(" a ")
                .append(dataFim.format(formatter))
                .append("\n\n");

        // Resumo geral
        texto.append("RESUMO GERAL\n");
        texto.append("-----------\n");
        texto.append("Total de Acessos: ").append(resumoGeral.get("totalAcessos")).append("\n");
        texto.append("Usuários Ativos: ").append(resumoGeral.get("usuariosAtivos")).append("\n");
        texto.append("Tempo Médio de Sessão: ").append(resumoGeral.get("tempoMedioSessaoMinutos")).append(" minutos\n");
        texto.append("Média de Acessos por Usuário: ").append(String.format("%.1f", resumoGeral.get("mediaAcessosPorUsuario"))).append("\n\n");

        // Estatísticas por usuário
        texto.append("ESTATÍSTICAS POR USUÁRIO\n");
        texto.append("------------------------\n");
        texto.append(String.format("%-30s %-10s %-20s %-15s %-15s\n",
                "USUÁRIO", "ACESSOS", "ÚLTIMO ACESSO", "TEMPO TOTAL", "PÁGINAS"));

        for (EstatisticaAcesso estatistica : estatisticasAcesso) {
            texto.append(String.format("%-30s %-10d %-20s %-15d %-15d\n",
                    estatistica.getNomeUsuario(),
                    estatistica.getQuantidadeAcessos(),
                    estatistica.getUltimoAcesso().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                    estatistica.getTempoTotalMinutos(),
                    estatistica.getPaginasVisitadas()));
        }

        return texto.toString();
    }

    @Override
    public String getTipo() {
        return "TEXT";
    }
}