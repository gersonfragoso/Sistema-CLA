package com.example.sistema_cla.domain.model;

public class Telefone {
    private Long id;
    private int ddd;
    private String numeroTelefone;

    public Telefone() {
    }

    public Telefone(Long id, int ddd, String numeroTelefone) {
        this.id = id;
        this.ddd = ddd;
        this.numeroTelefone = numeroTelefone;
    }


    public int getDdd() {
        return ddd;
    }

    public void setDdd(int ddd) {
        this.ddd = ddd;
    }

    public String getNumeroTelefone() {
        return numeroTelefone;
    }

    public void setNumeroTelefone(String numeroTelefone) {
        this.numeroTelefone = numeroTelefone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}