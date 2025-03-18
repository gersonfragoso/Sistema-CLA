package com.example.sistema_cla.application.service;

import com.example.sistema_cla.domain.model.Local;
import com.example.sistema_cla.domain.model.Usuario;
import com.example.sistema_cla.domain.repository.EntidadeNaoEncontradaException;
import com.example.sistema_cla.domain.repository.LocalRepository;
import com.example.sistema_cla.domain.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class BuscarEntidadeService {
    private final UsuarioRepository usuarioRepository;
    private final LocalRepository localRepository;

    public BuscarEntidadeService(UsuarioRepository usuarioRepository, LocalRepository localRepository) {
        this.usuarioRepository = usuarioRepository;
        this.localRepository = localRepository;
    }

    public Usuario buscarUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário", id));
    }

    public Local buscarLocal(Long id) {
        return localRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Local", id));
    }
}