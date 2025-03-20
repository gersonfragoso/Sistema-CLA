package com.example.sistema_cla.application.service;

import com.example.sistema_cla.infrastructure.utils.LocalMapper;
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
                .map(LocalMapper::toResponse)
                .collect(Collectors.toList());
    }

    public LocalResponse criarLocal(LocalRequest request) {
        Local novoLocal = LocalMapper.toEntity(request);
        Local salvo = localRepository.save(novoLocal);
        return LocalMapper.toResponse(salvo);
    }
}
