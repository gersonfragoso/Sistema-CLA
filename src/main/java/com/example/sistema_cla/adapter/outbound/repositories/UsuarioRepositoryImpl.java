package com.example.sistema_cla.adapter.outbound.repositories;

import com.example.sistema_cla.adapter.outbound.entities.JpaEnderecoEntity;
import com.example.sistema_cla.adapter.outbound.entities.JpaTelefoneEntity;
import com.example.sistema_cla.adapter.outbound.entities.JpaUsuarioEntity;
import com.example.sistema_cla.domain.model.Endereco;
import com.example.sistema_cla.domain.model.Telefone;
import com.example.sistema_cla.domain.model.Usuario;
import com.example.sistema_cla.domain.repository.UsuarioRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UsuarioRepositoryImpl implements UsuarioRepository {

    private final JpaUsuarioRepository jpaUsuarioRepository;

    public UsuarioRepositoryImpl(JpaUsuarioRepository jpaUsuarioRepository) {
        this.jpaUsuarioRepository = jpaUsuarioRepository;
    }

    @Override
    public Usuario save(Usuario usuario) {
        JpaUsuarioEntity entity = new JpaUsuarioEntity(
                usuario.getId(), // Pode ser null para novos usuários
                usuario.getNome(),
                usuario.getSobrenome(),
                usuario.getCpf(),
                usuario.getDataNascimento(),
                new JpaEnderecoEntity(
                        null,
                        usuario.getEndereco().getRua(),
                        usuario.getEndereco().getCidade(),
                        usuario.getEndereco().getEstado(),
                        usuario.getEndereco().getCep(),
                        usuario.getEndereco().getNumeroCasa()
                ),
                new JpaTelefoneEntity(null, usuario.getTelefone().getDdd(), usuario.getTelefone().getNumeroTelefone()),
                usuario.isBloqueado()
        );

        JpaUsuarioEntity savedEntity = jpaUsuarioRepository.save(entity);

        return new Usuario(
                savedEntity.getId(),
                savedEntity.getNome(),
                savedEntity.getSobrenome(),
                savedEntity.getCpf(),
                savedEntity.getDataNascimento(),
                new Endereco(
                        savedEntity.getId(),
                        savedEntity.getEndereco().getRua(),
                        savedEntity.getEndereco().getCidade(),
                        savedEntity.getEndereco().getEstado(),
                        savedEntity.getEndereco().getCep(),
                        savedEntity.getEndereco().getNumeroCasa()
                ),
                new Telefone(savedEntity.getId(), savedEntity.getTelefone().getDdd(), savedEntity.getTelefone().getNumeroTelefone()),
                savedEntity.isBloqueado()
        );
    }


    @Override
    public void bloquearUsuario(Long id, boolean bloquear) {
        Optional<JpaUsuarioEntity> usuarioEntity = jpaUsuarioRepository.findById(id);
        usuarioEntity.ifPresent(usuario -> {
            usuario.setBloqueado(bloquear);
            jpaUsuarioRepository.save(usuario);
        });
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return jpaUsuarioRepository.findById(id).map(entity ->
                new Usuario(
                        entity.getId(),
                        entity.getNome(),
                        entity.getSobrenome(),
                        entity.getCpf(),
                        entity.getDataNascimento(),
                        new Endereco(
                                entity.getId(),
                                entity.getEndereco().getRua(),
                                entity.getEndereco().getCidade(),
                                entity.getEndereco().getEstado(),
                                entity.getEndereco().getCep(),
                                entity.getEndereco().getNumeroCasa()
                        ),
                        new Telefone(entity.getId(), entity.getTelefone().getDdd(), entity.getTelefone().getNumeroTelefone()),
                        entity.isBloqueado()
                )
        );
    }
}
