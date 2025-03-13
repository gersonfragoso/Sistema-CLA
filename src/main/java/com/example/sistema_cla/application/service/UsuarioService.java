package com.example.sistema_cla.application.service;

import com.example.sistema_cla.infrastructure.exceptions.UsuarioNotFoundException;
import com.example.sistema_cla.presentation.dto.request.UsuarioRequest;
import com.example.sistema_cla.presentation.dto.response.UsuarioResponse;
import com.example.sistema_cla.domain.enums.TipoUsuario;
import com.example.sistema_cla.domain.model.Usuario;
import com.example.sistema_cla.domain.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<UsuarioResponse> listarUsuarios() {
        return usuarioRepository.findAll()
                .stream()
                .map(user -> new UsuarioResponse(
                        user.getId(),
                        user.getNome(),
                        user.getSobrenome(),
                        user.getEmail(),
                        user.getCpf()))
                .collect(Collectors.toList());
    }

    public UsuarioResponse buscarUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException(id));
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getSobrenome(),
                usuario.getEmail(),
                usuario.getCpf());
    }

    public UsuarioResponse criarUsuario(UsuarioRequest request) {
        Usuario novoUsuario = new Usuario(
                null, request.nome(), request.sobrenome(), request.cpf(), request.email(),
                request.senha(), request.dataNascimento(), TipoUsuario.VISITANTE,
                null, null, false, new ArrayList<>()
        );
        Usuario salvo = usuarioRepository.save(novoUsuario);
        return new UsuarioResponse(
                salvo.getId(),
                salvo.getNome(),
                salvo.getSobrenome(),
                salvo.getEmail(),
                salvo.getCpf());
    }
}