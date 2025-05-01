package com.example.sistema_cla.infrastructure.utils;

import com.example.sistema_cla.domain.enums.TipoUsuario;
import com.example.sistema_cla.domain.model.Endereco;
import com.example.sistema_cla.domain.model.Telefone;
import com.example.sistema_cla.domain.model.Usuario;
import com.example.sistema_cla.presentation.dto.request.EnderecoRequest;
import com.example.sistema_cla.presentation.dto.request.TelefoneRequest;
import com.example.sistema_cla.presentation.dto.request.UsuarioRequest;
import com.example.sistema_cla.presentation.dto.response.EnderecoResponse;
import com.example.sistema_cla.presentation.dto.response.TelefoneResponse;
import com.example.sistema_cla.presentation.dto.response.UsuarioResponse;

public class UsuarioMapper {

    public static Usuario toEntity(UsuarioRequest request) {
        Usuario usuario = new Usuario();
        usuario.setNome(request.nome());
        usuario.setSobrenome(request.sobrenome());
        usuario.setCpf(request.cpf());
        usuario.setEmail(request.email());
        usuario.setSenha(request.senha()); // Na prática, você deveria criptografar
        usuario.setDataNascimento(request.dataNascimento());
        usuario.setTipoUsuario(TipoUsuario.valueOf(request.tipoUsuario()));

        // Mapear Endereço
        if (request.endereco() != null) {
            usuario.setEndereco(toEnderecoEntity(request.endereco()));
        }

        // Mapear Telefone
        if (request.telefone() != null) {
            usuario.setTelefone(toTelefoneEntity(request.telefone()));
        }

        return usuario;
    }

    public static UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getSobrenome(),
                usuario.getEmail(),
                usuario.getCpf(),
                usuario.getDataNascimento(),
                usuario.getTipoUsuario(),
                usuario.getEndereco() != null ? toEnderecoResponse(usuario.getEndereco()) : null,
                usuario.getTelefone() != null ? toTelefoneResponse(usuario.getTelefone()) : null
        );
    }

    private static Endereco toEnderecoEntity(EnderecoRequest request) {
        if (request == null) return null;

        Endereco endereco = new Endereco();
        endereco.setLogradouro(request.logradouro());
        endereco.setNumero(request.numero());
        endereco.setComplemento(request.complemento());
        endereco.setBairro(request.bairro());
        endereco.setCidade(request.cidade());
        endereco.setEstado(request.estado());
        endereco.setCep(request.cep());
        endereco.setPais("Brasil"); // Default para Brasil

        return endereco;
    }

    private static Telefone toTelefoneEntity(TelefoneRequest request) {
        if (request == null) return null;

        Telefone telefone = new Telefone();
        telefone.setDdd(request.ddd());
        telefone.setNumeroTelefone(request.numeroTelefone());

        return telefone;
    }

    private static EnderecoResponse toEnderecoResponse(Endereco endereco) {
        if (endereco == null) return null;

        return new EnderecoResponse(
                endereco.getLogradouro(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getBairro(),
                endereco.getCidade(),
                endereco.getEstado(),
                endereco.getCep(),
                endereco.getEnderecoFormatado(),
                endereco.isValidado()
        );
    }

    private static TelefoneResponse toTelefoneResponse(Telefone telefone) {
        if (telefone == null) return null;

        return new TelefoneResponse(
                telefone.getDdd(),
                telefone.getNumeroTelefone(),
                telefone.getTelefoneFormatado()
        );
    }
}