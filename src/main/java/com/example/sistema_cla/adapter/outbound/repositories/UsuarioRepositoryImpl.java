package com.example.sistema_cla.adapter.outbound.repositories;

import com.example.sistema_cla.adapter.outbound.entities.JpaUsuarioEntity;
import com.example.sistema_cla.infrastructure.exceptions.DuplicateUserException;
import com.example.sistema_cla.infrastructure.utils.UsuarioMapper;
import com.example.sistema_cla.domain.model.Usuario;
import com.example.sistema_cla.domain.repository.UsuarioRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UsuarioRepositoryImpl implements UsuarioRepository {

    private final JpaUsuarioRepository jpaUsuarioRepository;

    public UsuarioRepositoryImpl(JpaUsuarioRepository jpaUsuarioRepository) {
        this.jpaUsuarioRepository = jpaUsuarioRepository;
    }

    @Override
    public Usuario save(Usuario usuario) {
        return UsuarioMapper.toDomainModel(
                jpaUsuarioRepository.save(UsuarioMapper.toJpaEntity(usuario))
        );
    }

    @Override
    public void bloquearUsuario(Long id, boolean bloquear) {
        jpaUsuarioRepository.findById(id)
                .ifPresent(usuario -> {
                    usuario.setBloqueado(bloquear);
                    jpaUsuarioRepository.save(usuario);
                });
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return jpaUsuarioRepository.findById(id)
                .map(UsuarioMapper::toDomainModel);
    }

    @Override
    public List<Usuario> findAll() {
        return jpaUsuarioRepository.findAll().stream()
                .map(UsuarioMapper::toDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Usuario> findByCpf(String cpf) {
        return jpaUsuarioRepository.findByCpf(cpf).map(UsuarioMapper::toDomainModel);
    }

    @Override
    public Optional<JpaUsuarioEntity> findByTelefoneDddAndNumero(String ddd, String numeroTelefone) {
        return jpaUsuarioRepository.findByTelefoneDddAndNumero(ddd,numeroTelefone);
    }
}