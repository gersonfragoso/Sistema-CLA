package com.example.sistema_cla.domain.model;

import com.example.sistema_cla.domain.enums.TipoUsuario;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class Usuario {
    private Long id;
    private String nome;
    private String sobrenome;
    private String cpf;
    private String email;
    private String senha;
    private LocalDate dataNascimento;
    private TipoUsuario tipoUsuario;
    private Endereco endereco;
    private Telefone telefone;
    private boolean bloqueado;

    // Método para obter o nome completo
    public String getNomeCompleto() {
        return nome + " " + sobrenome;
    }
}