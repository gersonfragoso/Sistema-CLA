package com.example.sistema_cla.application.service;

import com.example.sistema_cla.domain.model.Local;
import com.example.sistema_cla.domain.model.Usuario;
import com.example.sistema_cla.infrastructure.dao.interfaces.LocalDAO;
import com.example.sistema_cla.infrastructure.dao.interfaces.UsuarioDAO;
import com.example.sistema_cla.infrastructure.exceptions.EntidadeNaoEncontradaException;
import org.springframework.stereotype.Service;

@Service
public class BuscarEntidadeService {
    private final UsuarioDAO usuarioDAO;
    private final LocalDAO localDAO;

    public BuscarEntidadeService(UsuarioDAO usuarioDAO, LocalDAO localDAO) {
        this.usuarioDAO = usuarioDAO;
        this.localDAO = localDAO;
    }

    public Usuario buscarUsuario(Long id) {
        return usuarioDAO.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário", id));
    }

    public Local buscarLocal(Long id) {
        return localDAO.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Local", id));
    }
}