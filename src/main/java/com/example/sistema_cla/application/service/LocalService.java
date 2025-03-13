package com.example.sistema_cla.application.service;

import com.example.sistema_cla.domain.enums.StatusAcessibilidade;
import com.example.sistema_cla.infrastructure.exceptions.InvalidStatusAcessibilidadeException;
import com.example.sistema_cla.presentation.dto.request.LocalRequest;
import com.example.sistema_cla.presentation.dto.response.LocalResponse;
import com.example.sistema_cla.domain.model.Local;
import com.example.sistema_cla.domain.repository.LocalRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocalService {

    private final LocalRepository localRepository;

    public LocalService(LocalRepository localRepository) {
        this.localRepository = localRepository;
    }

    public List<LocalResponse> listarLocais() {
        return localRepository.findAll()
                .stream()
                .map(local -> new LocalResponse(
                        local.getNome(),
                        local.getEndereco(),
                        local.getTipoLocal(),
                        local.getStatusAcessibilidade().name()))
                .collect(Collectors.toList());
    }

    public LocalResponse criarLocal(LocalRequest request) {
        StatusAcessibilidade status;
        try {
            status = StatusAcessibilidade.valueOf(String.valueOf(request.statusAcessibilidade()).toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidStatusAcessibilidadeException(request.statusAcessibilidade());
        }
        Local novoLocal = new Local(null, request.nome(), request.endereco(),
                request.tipoLocal(), status, null);
        Local salvo = localRepository.save(novoLocal);
        return new LocalResponse(
                salvo.getNome(),
                salvo.getEndereco(),
                salvo.getTipoLocal(),
                salvo.getStatusAcessibilidade().name());
    }
}