package com.example.sistema_cla.application.service;

import com.example.sistema_cla.infrastructure.dao.interfaces.UsuarioDAO;
import com.example.sistema_cla.infrastructure.exceptions.ValidationException;
import com.example.sistema_cla.infrastructure.utils.UsuarioMapper;
import com.example.sistema_cla.presentation.dto.request.UsuarioRequest;
import com.example.sistema_cla.presentation.dto.response.UsuarioResponse;
import com.example.sistema_cla.domain.model.Usuario;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
    private final UsuarioDAO usuarioDAO;

    public UsuarioService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public List<UsuarioResponse> listarUsuarios() {
        return usuarioDAO.findAll()
                .stream()
                .map(UsuarioMapper::toResponse)
                .collect(Collectors.toList());
    }

    public UsuarioResponse criarUsuario(UsuarioRequest request) {
        validarCamposObrigatorios(request);
        validarEmailECpfExistentes(request);

        Usuario novoUsuario = UsuarioMapper.toEntity(request);
        Usuario salvo = usuarioDAO.save(novoUsuario);
        return UsuarioMapper.toResponse(salvo);
    }

    private void validarCamposObrigatorios(UsuarioRequest request) {
        List<String> camposFaltantes = new ArrayList<>();

        if (request.nome() == null || request.nome().trim().isEmpty()) {
            camposFaltantes.add("nome");
        }

        if (request.sobrenome() == null || request.sobrenome().trim().isEmpty()) {
            camposFaltantes.add("sobrenome");
        }

        if (request.email() == null || request.email().trim().isEmpty()) {
            camposFaltantes.add("email");
        }

        if (request.cpf() == null || request.cpf().trim().isEmpty()) {
            camposFaltantes.add("cpf");
        }

        if (request.senha() == null || request.senha().trim().isEmpty()) {
            camposFaltantes.add("senha");
        }

        if (!camposFaltantes.isEmpty()) {
            throw new ValidationException("Os seguintes campos são obrigatórios: " +
                    String.join(", ", camposFaltantes));
        }
    }

    private void validarEmailECpfExistentes(UsuarioRequest request) {
        if (request.email() != null && !request.email().trim().isEmpty()) {
            usuarioDAO.findByEmail(request.email()).ifPresent(u -> {
                throw new ValidationException("Já existe um usuário com o email: " + request.email());
            });
        }
        if (request.cpf() != null && !request.cpf().trim().isEmpty()) {
            usuarioDAO.findByCpf(request.cpf()).ifPresent(u -> {
                throw new ValidationException("Já existe um usuário com o CPF: " + request.cpf());
            });
        }
    }
}