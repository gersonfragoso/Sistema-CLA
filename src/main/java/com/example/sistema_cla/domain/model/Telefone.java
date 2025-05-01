package com.example.sistema_cla.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Telefone {
    private Long id;
    private int ddd;
    private String numeroTelefone;

    // Método para obter o telefone formatado
    public String getTelefoneFormatado() {
        return "(" + ddd + ") " + numeroTelefone;
    }
}