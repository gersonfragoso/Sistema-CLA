package com.example.sistema_cla.application.service;

import com.example.sistema_cla.adapter.outbound.repositories.JpaUsuarioRepository;
import com.example.sistema_cla.domain.model.Usuario;
import com.example.sistema_cla.domain.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario criarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public void bloquearUsuario(Long id, boolean bloquear) {
        usuarioRepository.bloquearUsuario(id, bloquear);
    }
}