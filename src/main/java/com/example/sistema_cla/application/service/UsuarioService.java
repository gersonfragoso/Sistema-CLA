package com.example.sistema_cla.application.service;

import com.example.sistema_cla.adapter.outbound.entities.JpaUsuarioEntity;
import com.example.sistema_cla.adapter.outbound.repositories.JpaUsuarioRepository;
import com.example.sistema_cla.domain.model.Usuario;
import com.example.sistema_cla.domain.repository.UsuarioRepository;
import com.example.sistema_cla.infrastructure.exceptions.DuplicateUserException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
    public List<Usuario> listarAll(){
        return usuarioRepository.findAll();
    }

    public Usuario criarUsuario(Usuario usuario) {
        // Validar o cpf
        String cpfNumerico = usuario.getCpf().replaceAll("[^0-9]", ""); // Remove tudo que não é número
        if (cpfNumerico.length() != 11) {
            throw new IllegalArgumentException("O CPF deve ter exatamente 11 dígitos. CPF fornecido: " + usuario.getCpf());
        }
        // Verificar duplicidade de CPF
        Optional<Usuario> existingByCpf = usuarioRepository.findByCpf(usuario.getCpf());
        if (existingByCpf.isPresent() && !existingByCpf.get().getId().equals(usuario.getId())) {
            throw new DuplicateUserException("Já existe um usuário com o CPF: " + usuario.getCpf());
        }

        // Verificar duplicidade de telefone
        Optional<JpaUsuarioEntity> existingByTelefone = usuarioRepository.findByTelefoneDddAndNumero(
                String.valueOf(usuario.getTelefone().getDdd()),
                usuario.getTelefone().getNumeroTelefone()
        );
        if (existingByTelefone.isPresent() && !existingByTelefone.get().getId().equals(usuario.getId())) {
            throw new DuplicateUserException("Já existe um usuário com o telefone: " +
                    usuario.getTelefone().getDdd() + " " + usuario.getTelefone().getNumeroTelefone());
        }
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public void bloquearUsuario(Long id, boolean bloquear) {
        usuarioRepository.bloquearUsuario(id, bloquear);
    }
}