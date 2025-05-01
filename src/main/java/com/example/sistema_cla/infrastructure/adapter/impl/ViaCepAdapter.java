package com.example.sistema_cla.infrastructure.adapter.impl;

import com.example.sistema_cla.domain.model.Endereco;
import com.example.sistema_cla.infrastructure.adapter.EnderecoValidatorService;
import com.example.sistema_cla.infrastructure.external.ViaCepClient;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

/**
 * Implementação do adaptador para o serviço ViaCEP
 * Implementa o padrão Adapter para adaptar o ViaCepClient para a interface EnderecoValidatorService
 */
@Service("viaCepService")
public class ViaCepAdapter implements EnderecoValidatorService {

    private final ViaCepClient viaCepClient;

    public ViaCepAdapter() {
        this.viaCepClient = new ViaCepClient();
    }

    @Override
    public Endereco validarEEnriquecerEndereco(Endereco endereco) {
        if (endereco == null) {
            throw new IllegalArgumentException("Endereço não pode ser nulo");
        }

        // Se tiver CEP, usa para validar e enriquecer
        if (endereco.getCep() != null && !endereco.getCep().isEmpty()) {
            JSONObject cepData = viaCepClient.consultarCep(endereco.getCep());

            if (cepData == null) {
                throw new IllegalArgumentException("CEP não encontrado ou inválido");
            }

            // Atualiza os campos vazios com dados da API
            if (endereco.getLogradouro() == null || endereco.getLogradouro().isEmpty()) {
                endereco.setLogradouro(cepData.getString("logradouro"));
            }

            if (endereco.getBairro() == null || endereco.getBairro().isEmpty()) {
                endereco.setBairro(cepData.getString("bairro"));
            }

            if (endereco.getCidade() == null || endereco.getCidade().isEmpty()) {
                endereco.setCidade(cepData.getString("localidade"));
            }

            if (endereco.getEstado() == null || endereco.getEstado().isEmpty()) {
                endereco.setEstado(cepData.getString("uf"));
            }

            // Marca como validado
            endereco.setValidado(true);
            return endereco;

        } else if (endereco.getLogradouro() != null && endereco.getCidade() != null && endereco.getEstado() != null) {
            // Se não tiver CEP mas tiver logradouro, cidade e estado, tenta validar
            JSONObject[] enderecos = viaCepClient.consultarEndereco(
                    endereco.getEstado(),
                    endereco.getCidade(),
                    endereco.getLogradouro()
            );

            if (enderecos == null || enderecos.length == 0) {
                throw new IllegalArgumentException("Endereço não encontrado ou inválido");
            }

            // Usa o primeiro endereço encontrado para enriquecer
            JSONObject primeiroEndereco = enderecos[0];

            if (endereco.getCep() == null || endereco.getCep().isEmpty()) {
                endereco.setCep(primeiroEndereco.getString("cep"));
            }

            if (endereco.getBairro() == null || endereco.getBairro().isEmpty()) {
                endereco.setBairro(primeiroEndereco.getString("bairro"));
            }

            // Marca como validado
            endereco.setValidado(true);
            return endereco;
        } else {
            throw new IllegalArgumentException("Informações insuficientes para validar o endereço");
        }
    }

    @Override
    public boolean isCepValido(String cep) {
        if (cep == null || cep.isEmpty()) {
            return false;
        }

        // Remove caracteres não numéricos para validação
        String cepNumerico = cep.replaceAll("\\D", "");

        // Verifica se o CEP tem 8 dígitos
        if (cepNumerico.length() != 8) {
            return false;
        }

        // Consulta o CEP na API ViaCEP
        JSONObject cepData = viaCepClient.consultarCep(cep);
        return cepData != null;
    }
}