package com.example.sistema_cla.infrastructure.adapter;

import com.example.sistema_cla.domain.model.Endereco;

/**
 * Interface para validação e enriquecimento de endereços
 */
public interface EnderecoValidatorService {

    /**
     * Valida e enriquece um endereço com informações adicionais
     * @param endereco O endereço a ser validado
     * @return O endereço validado e enriquecido
     * @throws IllegalArgumentException Se o endereço for inválido
     */
    Endereco validarEEnriquecerEndereco(Endereco endereco);

    /**
     * Verifica se um CEP é válido
     * @param cep O CEP a ser validado
     * @return true se o CEP for válido
     */
    boolean isCepValido(String cep);
}