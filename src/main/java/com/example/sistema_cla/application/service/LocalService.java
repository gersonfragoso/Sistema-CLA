package com.example.sistema_cla.application.service;

import com.example.sistema_cla.infrastructure.dao.interfaces.LocalDAO;
import com.example.sistema_cla.infrastructure.utils.LocalMapper;
import com.example.sistema_cla.presentation.dto.request.LocalRequest;
import com.example.sistema_cla.presentation.dto.response.LocalComAvaliacoesResponse;
import com.example.sistema_cla.presentation.dto.response.LocalResponse;
import com.example.sistema_cla.domain.model.Local;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocalService {
    private final LocalDAO localDAO;
    private final BuscarEntidadeService buscarEntidadeService;

    public LocalService(LocalDAO localDAO, BuscarEntidadeService buscarEntidadeService) {
        this.localDAO = localDAO;
        this.buscarEntidadeService = buscarEntidadeService;
    }

    public List<LocalResponse> listarLocais() {
        return localDAO.findAll()
                .stream()
                .map(LocalMapper::toResponse)
                .collect(Collectors.toList());
    }

    public LocalResponse criarLocal(LocalRequest request) {
        Local novoLocal = LocalMapper.toEntity(request);
        Local salvo = localDAO.save(novoLocal);
        return LocalMapper.toResponse(salvo);
    }
    public LocalComAvaliacoesResponse buscarLocalDetalhado(Long id) {
        return buscarEntidadeService.buscarLocalComAvaliacoes(id);
    }
}