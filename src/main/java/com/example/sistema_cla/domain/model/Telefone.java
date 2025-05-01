package com.example.sistema_cla.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Telefone {

    private Long id;

    private int ddd;
    private String numeroTelefone;
}
