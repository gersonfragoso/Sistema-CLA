package com.example.sistema_cla.application.service;

import com.example.sistema_cla.infrastructure.dao.interfaces.AvaliacaoDAO;
import com.example.sistema_cla.infrastructure.exceptions.ValidationException;
import com.example.sistema_cla.infrastructure.utils.AvaliacaoMapper;
import com.example.sistema_cla.presentation.dto.request.AvaliacaoRequest;
import com.example.sistema_cla.presentation.dto.response.AvaliacaoResponse;
import com.example.sistema_cla.domain.model.Avaliacao;
import com.example.sistema_cla.domain.model.Local;
import com.example.sistema_cla.domain.model.Usuario;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AvaliacaoService {
    private final AvaliacaoDAO avaliacaoDAO;
    private final BuscarEntidadeService buscarEntidadeService;

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
        return AvaliacaoMapper.toResponse(salvo);
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