package com.example.sistema_cla.domain.model;

import com.example.sistema_cla.domain.enums.TipoUsuario;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    private List<Avaliacao> avaliacoes;
}

