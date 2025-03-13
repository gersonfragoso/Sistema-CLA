package com.example.sistema_cla.presentation.dto.request;

import java.time.LocalDate;

public record UsuarioRequest(
        String nome,
        String sobrenome,
        String email,
        String cpf,
        String senha,
        LocalDate dataNascimento
) {}