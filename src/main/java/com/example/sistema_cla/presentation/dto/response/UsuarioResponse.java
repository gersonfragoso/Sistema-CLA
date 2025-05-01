package com.example.sistema_cla.presentation.dto.response;

import com.example.sistema_cla.domain.enums.TipoUsuario;

import java.time.LocalDate;

public record UsuarioResponse(
        Long id,
        String nome,
        String sobrenome,
        String email,
        String cpf,
        LocalDate dataNascimento,
        TipoUsuario tipoUsuario,
        EnderecoResponse endereco,
        TelefoneResponse telefone
) {}