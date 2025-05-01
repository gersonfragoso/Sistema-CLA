package com.example.sistema_cla.application.service;

import com.example.sistema_cla.domain.model.AvaliacaoMemento;
import com.example.sistema_cla.domain.model.HistoricoAvaliacao;
import com.example.sistema_cla.infrastructure.dao.interfaces.AvaliacaoDAO;
import com.example.sistema_cla.infrastructure.exceptions.ValidationException;
import com.example.sistema_cla.infrastructure.utils.AvaliacaoMapper;
import com.example.sistema_cla.presentation.dto.request.AvaliacaoRequest;
import com.example.sistema_cla.presentation.dto.response.AvaliacaoResponse;
import com.example.sistema_cla.domain.model.Avaliacao;
import com.example.sistema_cla.domain.model.Local;
import com.example.sistema_cla.domain.model.Usuario;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AvaliacaoService {
    private final AvaliacaoDAO avaliacaoDAO;
    private final BuscarEntidadeService buscarEntidadeService;

    // Armazena históricos por ID de avaliação
    private final Map<Long, HistoricoAvaliacao> historicosAvaliacoes = new HashMap<>();

    public AvaliacaoService(AvaliacaoDAO avaliacaoDAO, BuscarEntidadeService buscarEntidadeService) {
        this.avaliacaoDAO = avaliacaoDAO;
        this.buscarEntidadeService = buscarEntidadeService;
    }

    public AvaliacaoResponse criarAvaliacao(AvaliacaoRequest request) {
        validarCamposObrigatorios(request);
        Usuario usuario = buscarEntidadeService.buscarUsuario(request.usuarioId());
        Local local = buscarEntidadeService.buscarLocal(request.localId());

        Avaliacao avaliacao = AvaliacaoMapper.toEntity(request, usuario, local);
        Avaliacao salvo = avaliacaoDAO.save(avaliacao);

        // Cria um histórico para a nova avaliação
        HistoricoAvaliacao historico = new HistoricoAvaliacao();
        historico.salvarEstado(salvo.salvar());
        historicosAvaliacoes.put(salvo.getId(), historico);

        return AvaliacaoMapper.toResponse(salvo);
    }

    public AvaliacaoResponse editarAvaliacao(Long avaliacaoId, AvaliacaoRequest request) {
        validarCamposObrigatorios(request);

        Avaliacao avaliacao = avaliacaoDAO.findById(avaliacaoId)
                .orElseThrow(() -> new ValidationException("Avaliação não encontrada"));

        // Salva o estado atual antes da edição
        HistoricoAvaliacao historico = historicosAvaliacoes.computeIfAbsent(
                avaliacaoId, id -> new HistoricoAvaliacao());
        historico.salvarEstado(avaliacao.salvar());

        // Atualiza a avaliação
        avaliacao.editar(request.comentario(), request.nota());

        // Se mudou o usuário ou local, atualize
        if (!avaliacao.getUsuario().getId().equals(request.usuarioId())) {
            Usuario novoUsuario = buscarEntidadeService.buscarUsuario(request.usuarioId());
            avaliacao.setUsuario(novoUsuario);
        }

        if (!avaliacao.getLocalAcessivel().getId().equals(request.localId())) {
            Local novoLocal = buscarEntidadeService.buscarLocal(request.localId());
            avaliacao.setLocalAcessivel(novoLocal);
        }

        Avaliacao atualizada = avaliacaoDAO.save(avaliacao);
        return AvaliacaoMapper.toResponse(atualizada);
    }

    public AvaliacaoResponse desfazerEdicao(Long avaliacaoId) {
        Avaliacao avaliacao = avaliacaoDAO.findById(avaliacaoId)
                .orElseThrow(() -> new ValidationException("Avaliação não encontrada"));

        HistoricoAvaliacao historico = historicosAvaliacoes.get(avaliacaoId);
        if (historico == null || !historico.temEstadosParaDesfazer()) {
            throw new ValidationException("Não há alterações para desfazer");
        }

        AvaliacaoMemento estadoAnterior = historico.desfazer();
        avaliacao.editar(estadoAnterior);

        Avaliacao restaurada = avaliacaoDAO.save(avaliacao);
        return AvaliacaoMapper.toResponse(restaurada);
    }

    private void validarCamposObrigatorios(AvaliacaoRequest request) {
        List<String> erros = new ArrayList<>();

        if (request.usuarioId() == null) {
            erros.add("O ID do usuário é obrigatório");
        }

        if (request.localId() == null) {
            erros.add("O ID do local é obrigatório");
        }

        if (request.nota() < 1 || request.nota() > 5) {
            erros.add("A nota deve estar entre 1 e 5");
        }

        if (request.comentario() != null && request.comentario().length() > 500) {
            erros.add("O comentário deve ter no máximo 500 caracteres");
        }

        if (!erros.isEmpty()) {
            throw new ValidationException(String.join("; ", erros));
        }
    }
}