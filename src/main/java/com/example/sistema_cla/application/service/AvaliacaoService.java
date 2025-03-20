package com.example.sistema_cla.application.service;

import com.example.sistema_cla.infrastructure.utils.AvaliacaoMapper;
import com.example.sistema_cla.presentation.dto.request.AvaliacaoRequest;
import com.example.sistema_cla.presentation.dto.response.AvaliacaoResponse;
import com.example.sistema_cla.domain.model.Avaliacao;
import com.example.sistema_cla.domain.model.Local;
import com.example.sistema_cla.domain.model.Usuario;
import com.example.sistema_cla.domain.repository.AvaliacaoRepository;
import org.springframework.stereotype.Service;

@Service
public class AvaliacaoService {
    private final AvaliacaoRepository avaliacaoRepository;
    private final BuscarEntidadeService buscarEntidadeService;

    public AvaliacaoService(AvaliacaoRepository avaliacaoRepository, BuscarEntidadeService buscarEntidadeService) {
        this.avaliacaoRepository = avaliacaoRepository;
        this.buscarEntidadeService = buscarEntidadeService;
    }

    public AvaliacaoResponse criarAvaliacao(AvaliacaoRequest request) {
        Usuario usuario = buscarEntidadeService.buscarUsuario(request.usuarioId());
        Local local = buscarEntidadeService.buscarLocal(request.localId());

        Avaliacao avaliacao = AvaliacaoMapper.toEntity(request, usuario, local);
        Avaliacao salvo = avaliacaoRepository.save(avaliacao);

        return AvaliacaoMapper.toResponse(salvo);
    }
}