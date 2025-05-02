package com.example.sistema_cla.domain.observer.impl;

import com.example.sistema_cla.domain.model.EstatisticaAcesso;
import com.example.sistema_cla.domain.model.RegistroAcesso;
import com.example.sistema_cla.domain.observer.AcessoObserver;
import com.example.sistema_cla.infrastructure.dao.interfaces.EstatisticaAcessoDAO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class EstatisticaAcessoObserver implements AcessoObserver {
    private final EstatisticaAcessoDAO estatisticaAcessoDAO;

    public EstatisticaAcessoObserver(EstatisticaAcessoDAO estatisticaAcessoDAO) {
        this.estatisticaAcessoDAO = estatisticaAcessoDAO;
    }

    @Override
    public void onNovoAcesso(RegistroAcesso registroAcesso) {
        // Buscar ou criar estatística para o usuário
        EstatisticaAcesso estatistica = estatisticaAcessoDAO
                .findByUsuarioId(registroAcesso.getUsuario().getId())
                .orElse(new EstatisticaAcesso());

        // Atualizar estatísticas
        estatistica.setUsuarioId(registroAcesso.getUsuario().getId());
        estatistica.setNomeUsuario(registroAcesso.getUsuario().getNomeCompleto());
        estatistica.setQuantidadeAcessos(estatistica.getQuantidadeAcessos() + 1);
        estatistica.setUltimoAcesso(registroAcesso.getDataAcesso());
        estatistica.setTempoTotalMinutos(estatistica.getTempoTotalMinutos() + registroAcesso.getTempoSessaoMinutos());
        estatistica.setPaginasVisitadas(estatistica.getPaginasVisitadas() + registroAcesso.getPaginasVisitadas());

        // Salvar estatística atualizada
        estatisticaAcessoDAO.save(estatistica);

        System.out.println("Estatísticas de acesso atualizadas para o usuário: " + estatistica.getNomeUsuario());
    }
}