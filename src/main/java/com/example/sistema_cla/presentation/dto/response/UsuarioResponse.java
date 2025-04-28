package com.example.sistema_cla.presentation.dto.response;

public record UsuarioResponse(
        Long id,
        String nome,
        String sobrenome,
        String email,
        String cpf
) {}
