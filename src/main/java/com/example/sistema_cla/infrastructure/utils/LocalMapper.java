package com.example.sistema_cla.infrastructure.utils;

import com.example.sistema_cla.domain.model.Local;
import com.example.sistema_cla.presentation.dto.request.LocalRequest;
import com.example.sistema_cla.presentation.dto.response.LocalResponse;

public class LocalMapper {
    public static LocalResponse toResponse(Local local) {
        return new LocalResponse(
                local.getNome(),
                local.getEndereco(),
                local.getTipoLocal(),
                local.getStatusAcessibilidade().name()
        );
    }

    public static Local toEntity(LocalRequest request) {
        return new Local(null, request.nome(), request.endereco(),
                request.tipoLocal(), request.statusAcessibilidade(), null);
    }
}

