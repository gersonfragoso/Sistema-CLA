package com.example.sistema_cla.presentation.dto.response;

public record EnderecoResponse(
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String estado,
        String cep,
        String enderecoFormatado,
        boolean validado
) {}