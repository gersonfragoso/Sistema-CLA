package com.example.sistema_cla.infrastructure.dao.interfaces;

import com.example.sistema_cla.domain.model.Usuario;

import java.util.Optional;

public interface UsuarioDAO extends GenericDAO<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByCpf(String cpf);
}