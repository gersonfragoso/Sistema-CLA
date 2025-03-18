package com.example.sistema_cla.infrastructure.utils;

import com.example.sistema_cla.domain.enums.TipoUsuario;
import com.example.sistema_cla.domain.model.Usuario;
import com.example.sistema_cla.presentation.dto.request.UsuarioRequest;
import com.example.sistema_cla.presentation.dto.response.UsuarioResponse;

import java.util.ArrayList;

public class UsuarioMapper {
    public static UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getSobrenome(),
                usuario.getEmail(),
                usuario.getCpf()
        );
    }

    public static Usuario toEntity(UsuarioRequest request) {
        return new Usuario(null, request.nome(), request.sobrenome(), request.cpf(), request.email(),
                request.senha(), request.dataNascimento(), TipoUsuario.VISITANTE, null, null, false, new ArrayList<>());
    }
}
