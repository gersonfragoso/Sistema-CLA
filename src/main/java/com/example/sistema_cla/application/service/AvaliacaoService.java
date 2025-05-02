package com.example.sistema_cla.application.service;

import com.example.sistema_cla.domain.command.AvaliacaoCommand;
import com.example.sistema_cla.domain.command.CommandInvoker;
import com.example.sistema_cla.domain.command.impl.CriarAvaliacaoCommand;
import com.example.sistema_cla.domain.command.impl.EditarAvaliacaoCommand;
import com.example.sistema_cla.domain.model.Avaliacao;
import com.example.sistema_cla.domain.model.HistoricoAvaliacao;
import com.example.sistema_cla.domain.model.Local;
import com.example.sistema_cla.domain.model.Usuario;
import com.example.sistema_cla.infrastructure.dao.interfaces.AvaliacaoDAO;
import com.example.sistema_cla.infrastructure.dao.interfaces.LocalDAO;
import com.example.sistema_cla.infrastructure.dao.interfaces.UsuarioDAO;
import com.example.sistema_cla.infrastructure.exceptions.EntityNotFoundException;
import com.example.sistema_cla.infrastructure.utils.AvaliacaoMapper;
import com.example.sistema_cla.presentation.dto.request.AvaliacaoRequest;
import com.example.sistema_cla.presentation.dto.response.AvaliacaoResponse;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AvaliacaoService {
    private final AvaliacaoDAO avaliacaoDAO;
    private final UsuarioDAO usuarioDAO;
    private final LocalDAO localDAO;
    private final CommandInvoker commandInvoker;

    // Mapa para armazenar os históricos de cada avaliação
    private final Map<Long, HistoricoAvaliacao> historicos = new HashMap<>();

    public AvaliacaoService(
            AvaliacaoDAO avaliacaoDAO,
            UsuarioDAO usuarioDAO,
            LocalDAO localDAO,
            CommandInvoker commandInvoker) {
        this.avaliacaoDAO = avaliacaoDAO;
        this.usuarioDAO = usuarioDAO;
        this.localDAO = localDAO;
        this.commandInvoker = commandInvoker;
    }

    public AvaliacaoResponse criarAvaliacao(AvaliacaoRequest request) {
        Usuario usuario = usuarioDAO.findById(request.usuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario", request.usuarioId()));

        Local local = localDAO.findById(request.localId())
                .orElseThrow(() -> new EntityNotFoundException("Local", request.localId()));

        Avaliacao avaliacao = AvaliacaoMapper.toEntity(request, usuario, local);

        // Criar um novo histórico para a avaliação
        HistoricoAvaliacao historico = new HistoricoAvaliacao();

        // Criar e executar comando
        AvaliacaoCommand command = new CriarAvaliacaoCommand(avaliacao, avaliacaoDAO, historico);
        commandInvoker.executarComando(command);

        // Armazenar o histórico no mapa
        historicos.put(avaliacao.getId(), historico);

        return AvaliacaoMapper.toResponse(avaliacao);
    }

    public AvaliacaoResponse editarAvaliacao(Long id, AvaliacaoRequest request) {
        Avaliacao avaliacao = avaliacaoDAO.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Avaliação", id));

        // Obter ou criar histórico para esta avaliação
        HistoricoAvaliacao historico = historicos.computeIfAbsent(id, k -> new HistoricoAvaliacao());

        // Criar e executar comando
        AvaliacaoCommand command = new EditarAvaliacaoCommand(
                avaliacao,
                request.comentario(),
                request.nota(),
                avaliacaoDAO,
                historico
        );
        commandInvoker.executarComando(command);

        return AvaliacaoMapper.toResponse(avaliacao);
    }
    public AvaliacaoResponse buscarPorId(Long id) {
        Avaliacao avaliacao = avaliacaoDAO.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Avaliação", id));

        return AvaliacaoMapper.toResponse(avaliacao);
    }

    public AvaliacaoResponse desfazerEdicao(Long id) {
        Avaliacao avaliacao = avaliacaoDAO.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Avaliação", id));

        // Desfazer último comando
        commandInvoker.desfazerUltimoComando();

        return AvaliacaoMapper.toResponse(avaliacao);
    }
}