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

        // Validar o cpf
        String cpfNumerico = usuario.getCpf().replaceAll("[^0-9]", ""); // Remove tudo que não é número
        if (cpfNumerico.length() != 11) {
            throw new IllegalArgumentException("O CPF deve ter exatamente 11 dígitos. CPF fornecido: " + usuario.getCpf());
        }
        // Verificar duplicidade de CPF
        Optional<JpaUsuarioEntity> existingByCpf = jpaUsuarioRepository.findByCpf(usuario.getCpf());
        if (existingByCpf.isPresent() && !existingByCpf.get().getId().equals(usuario.getId())) {
            throw new DuplicateUserException("Já existe um usuário com o CPF: " + usuario.getCpf());
        }

        // Verificar duplicidade de telefone
        Optional<JpaUsuarioEntity> existingByTelefone = jpaUsuarioRepository.findByTelefoneDddAndNumero(
                String.valueOf(usuario.getTelefone().getDdd()),
                usuario.getTelefone().getNumeroTelefone()
        );
        if (existingByTelefone.isPresent() && !existingByTelefone.get().getId().equals(usuario.getId())) {
            throw new DuplicateUserException("Já existe um usuário com o telefone: " +
                    usuario.getTelefone().getDdd() + " " + usuario.getTelefone().getNumeroTelefone());
        }
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
}