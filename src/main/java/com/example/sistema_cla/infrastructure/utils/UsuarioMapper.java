package com.example.sistema_cla.infrastructure.utils;

import com.example.sistema_cla.adapter.outbound.entities.JpaEnderecoEntity;
import com.example.sistema_cla.adapter.outbound.entities.JpaTelefoneEntity;
import com.example.sistema_cla.adapter.outbound.entities.JpaUsuarioEntity;
import com.example.sistema_cla.domain.model.Endereco;
import com.example.sistema_cla.domain.model.Telefone;
import com.example.sistema_cla.domain.model.Usuario;

public class UsuarioMapper {

    public static JpaUsuarioEntity toJpaEntity(Usuario usuario) {
        if (usuario == null) return null;
        return new JpaUsuarioEntity(
                usuario.getId(),
                usuario.getNome(),
                usuario.getSobrenome(),
                usuario.getCpf(),
                usuario.getDataNascimento(),
                toJpaEndereco(usuario.getEndereco()),
                toJpaTelefone(usuario.getTelefone()),
                usuario.isBloqueado()
        );
    }

    public static JpaEnderecoEntity toJpaEndereco(Endereco endereco) {
        if (endereco == null) return null;
        return new JpaEnderecoEntity(
                endereco.getId(),
                endereco.getRua(),
                endereco.getCidade(),
                endereco.getEstado(),
                endereco.getCep(),
                endereco.getNumeroCasa()
        );
    }

    public static JpaTelefoneEntity toJpaTelefone(Telefone telefone) {
        if (telefone == null) return null;
        return new JpaTelefoneEntity(
                telefone.getId(),
                telefone.getDdd(),
                telefone.getNumeroTelefone()
        );
    }

    public static Usuario toDomainModel(JpaUsuarioEntity entity) {
        if (entity == null) return null;
        return new Usuario(
                entity.getId(),
                entity.getNome(),
                entity.getSobrenome(),
                entity.getCpf(),
                entity.getDataNascimento(),
                toDomainEndereco(entity.getEndereco()),
                toDomainTelefone(entity.getTelefone()),
                entity.isBloqueado()
        );
    }

    public static Endereco toDomainEndereco(JpaEnderecoEntity entity) {
        if (entity == null) return null;
        return new Endereco(
                entity.getId(),
                entity.getRua(),
                entity.getCidade(),
                entity.getEstado(),
                entity.getCep(),
                entity.getNumeroCasa()
        );
    }

    public static Telefone toDomainTelefone(JpaTelefoneEntity entity) {
        if (entity == null) return null;
        return new Telefone(
                entity.getId(),
                entity.getDdd(),
                entity.getNumeroTelefone()
        );
    }
}