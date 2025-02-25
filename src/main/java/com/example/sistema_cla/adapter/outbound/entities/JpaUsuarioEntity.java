package com.example.sistema_cla.adapter.outbound.entities;

import jakarta.persistence.*;

import java.time.LocalDate;


@Entity
public class JpaUsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String sobrenome;
    private String cpf;
    private LocalDate dataNascimento;

    @OneToOne(cascade = CascadeType.ALL)
    private JpaEnderecoEntity endereco;

    @OneToOne(cascade = CascadeType.ALL)
    private JpaTelefoneEntity telefone;

    private boolean bloqueado;

    // Construtor completo
    public JpaUsuarioEntity(Long id, String nome, String sobrenome, String cpf, LocalDate dataNascimento,
                            JpaEnderecoEntity endereco, JpaTelefoneEntity telefone, boolean bloqueado) {
        this.id = id;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.endereco = endereco;
        this.telefone = telefone;
        this.bloqueado = bloqueado;
    }

    public JpaUsuarioEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public JpaEnderecoEntity getEndereco() {
        return endereco;
    }

    public void setEndereco(JpaEnderecoEntity endereco) {
        this.endereco = endereco;
    }

    public JpaTelefoneEntity getTelefone() {
        return telefone;
    }

    public void setTelefone(JpaTelefoneEntity telefone) {
        this.telefone = telefone;
    }

    public boolean isBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(boolean bloqueado) {
        this.bloqueado = bloqueado;
    }
}