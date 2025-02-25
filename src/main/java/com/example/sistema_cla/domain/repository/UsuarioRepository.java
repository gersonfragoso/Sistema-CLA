package com.example.sistema_cla.domain.repository;

import com.example.sistema_cla.domain.model.Usuario;

import java.util.Optional;

public interface UsuarioRepository {
    Usuario save(Usuario usuario);

    void bloquearUsuario(Long id, boolean bloquear);

    Optional<Usuario> findById(Long id);
}
