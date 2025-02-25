package com.example.sistema_cla.domain.repository;

import com.example.sistema_cla.adapter.outbound.entities.JpaUsuarioEntity;
import com.example.sistema_cla.domain.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {
    Usuario save(Usuario usuario);

    void bloquearUsuario(Long id, boolean bloquear);

    Optional<Usuario> findById(Long id);

    List<Usuario> findAll();

    Optional<Usuario> findByCpf(String cpf);

    Optional<JpaUsuarioEntity> findByTelefoneDddAndNumero(String ddd, String numeroTelefone);


}
