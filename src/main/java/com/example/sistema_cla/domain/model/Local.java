package com.example.sistema_cla.domain.model;

import com.example.sistema_cla.domain.enums.StatusAcessibilidade;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Local {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String endereco;
    private String tipoLocal; // Exemplo: Restaurante, Hotel, Shopping

    @Enumerated(EnumType.STRING)
    private StatusAcessibilidade statusAcessibilidade; // A, P, I

    @OneToMany(mappedBy = "localAcessivel", cascade = CascadeType.ALL)
    private List<Avaliacao> avaliacoes;
}


