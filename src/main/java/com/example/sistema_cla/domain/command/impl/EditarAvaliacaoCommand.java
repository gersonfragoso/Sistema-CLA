package com.example.sistema_cla.domain.command.impl;

import com.example.sistema_cla.domain.command.AvaliacaoCommand;
import com.example.sistema_cla.domain.model.Avaliacao;
import com.example.sistema_cla.domain.model.AvaliacaoMemento;
import com.example.sistema_cla.domain.model.HistoricoAvaliacao;
import com.example.sistema_cla.infrastructure.dao.interfaces.AvaliacaoDAO;


public class EditarAvaliacaoCommand implements AvaliacaoCommand {
    private final Avaliacao avaliacao;
    private final String novoComentario;
    private final int novaNota;
    private final AvaliacaoDAO avaliacaoDAO;
    private final HistoricoAvaliacao historico;

    public EditarAvaliacaoCommand(
            Avaliacao avaliacao,
            String novoComentario,
            int novaNota,
            AvaliacaoDAO avaliacaoDAO,
            HistoricoAvaliacao historico) {
        this.avaliacao = avaliacao;
        this.novoComentario = novoComentario;
        this.novaNota = novaNota;
        this.avaliacaoDAO = avaliacaoDAO;
        this.historico = historico;
    }

    @Override
    public void execute() {
        // Salvar estado atual antes da operação
        AvaliacaoMemento estadoAtual = avaliacao.salvar();
        historico.salvarEstado(estadoAtual);

        // Executar a operação
        avaliacao.editar(novoComentario, novaNota);
        avaliacaoDAO.save(avaliacao);
    }

    @Override
    public void undo() {
        if (historico.temEstadosParaDesfazer()) {
            AvaliacaoMemento estadoAnterior = historico.desfazer();
            avaliacao.editar(estadoAnterior);
            avaliacaoDAO.save(avaliacao);
        }
    }
}