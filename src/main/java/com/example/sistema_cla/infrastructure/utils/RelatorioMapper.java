package com.example.sistema_cla.infrastructure.utils;

import com.example.sistema_cla.domain.model.Relatorio;
import com.example.sistema_cla.domain.model.Usuario;
import com.example.sistema_cla.presentation.dto.request.RelatorioEstatisticaRequest;
import com.example.sistema_cla.presentation.dto.response.RelatorioResponse;

import java.time.format.DateTimeFormatter;

public class RelatorioMapper {

    public static Relatorio toEntityFromEstatisticaRequest(RelatorioEstatisticaRequest request, Usuario usuario) {
        Relatorio relatorio = new Relatorio();
        relatorio.setTitulo(request.titulo());
        relatorio.setTipo(request.formato());
        relatorio.setCategoria("ESTATISTICA_ACESSO");
        relatorio.setDataGeracao(java.time.LocalDate.now());
        relatorio.setUsuario(usuario);
        relatorio.setPeriodoInicio(request.dataInicio());
        relatorio.setPeriodoFim(request.dataFim());
        return relatorio;
    }

    public static RelatorioResponse toResponse(Relatorio relatorio) {
        String periodoFormatado = "";
        if (relatorio.getPeriodoInicio() != null && relatorio.getPeriodoFim() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            periodoFormatado = relatorio.getPeriodoInicio().format(formatter) +
                    " a " +
                    relatorio.getPeriodoFim().format(formatter);
        }

        return new RelatorioResponse(
                relatorio.getId(),
                relatorio.getTitulo(),
                relatorio.getDataGeracao(),
                relatorio.getTipo(),
                relatorio.getCategoria(),
                periodoFormatado,
                relatorio.getConteudo(),
                relatorio.getUsuario() != null ? relatorio.getUsuario().getId() : null
        );
    }
}