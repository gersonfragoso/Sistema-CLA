package com.example.sistema_cla.domain.model;

import com.example.sistema_cla.domain.enums.StatusAcessibilidade;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Local {

    private Long id;

    private String nome;
    private String endereco;
    private String tipoLocal; // Exemplo: Restaurante, Hotel, Shopping

    private StatusAcessibilidade statusAcessibilidade; // A, P, I

    private List<Avaliacao> avaliacoes;
}


