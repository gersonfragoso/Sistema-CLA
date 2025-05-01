package com.example.sistema_cla.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Endereco {

    private Long id;
    private String rua;
    private String cidade;
    private String estado;
    private String cep;
    private int numeroCasa;
}
