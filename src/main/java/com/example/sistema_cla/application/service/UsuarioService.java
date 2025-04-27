package com.example.sistema_cla.application.service;

import com.example.sistema_cla.infrastructure.dao.interfaces.UsuarioDAO;
import com.example.sistema_cla.infrastructure.utils.UsuarioMapper;
import com.example.sistema_cla.presentation.dto.request.UsuarioRequest;
import com.example.sistema_cla.presentation.dto.response.UsuarioResponse;
import com.example.sistema_cla.domain.model.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
    private final UsuarioDAO usuarioDAO;

    public UsuarioService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public List<UsuarioResponse> listarUsuarios() {
        return usuarioDAO.findAll()
                .stream()
                .map(UsuarioMapper::toResponse)
                .collect(Collectors.toList());
    }

    public UsuarioResponse criarUsuario(UsuarioRequest request) {
        Usuario novoUsuario = UsuarioMapper.toEntity(request);
        Usuario salvo = usuarioDAO.save(novoUsuario);
        return UsuarioMapper.toResponse(salvo);
    }
}