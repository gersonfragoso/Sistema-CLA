package com.example.sistema_cla.application.service;

import com.example.sistema_cla.infrastructure.dao.interfaces.AvaliacaoDAO;
import com.example.sistema_cla.infrastructure.utils.AvaliacaoMapper;
import com.example.sistema_cla.presentation.dto.request.AvaliacaoRequest;
import com.example.sistema_cla.presentation.dto.response.AvaliacaoResponse;
import com.example.sistema_cla.domain.model.Avaliacao;
import com.example.sistema_cla.domain.model.Local;
import com.example.sistema_cla.domain.model.Usuario;
import org.springframework.stereotype.Service;

@Service
public class AvaliacaoService {
    private final AvaliacaoDAO avaliacaoDAO;
    private final BuscarEntidadeService buscarEntidadeService;

    public AvaliacaoService(AvaliacaoDAO avaliacaoDAO, BuscarEntidadeService buscarEntidadeService) {
        this.avaliacaoDAO = avaliacaoDAO;
        this.buscarEntidadeService = buscarEntidadeService;
    }

    public AvaliacaoResponse criarAvaliacao(AvaliacaoRequest request) {
        Usuario usuario = buscarEntidadeService.buscarUsuario(request.usuarioId());
        Local local = buscarEntidadeService.buscarLocal(request.localId());

        Avaliacao avaliacao = AvaliacaoMapper.toEntity(request, usuario, local);
        Avaliacao salvo = avaliacaoDAO.save(avaliacao);

        return AvaliacaoMapper.toResponse(salvo);
    }
}
