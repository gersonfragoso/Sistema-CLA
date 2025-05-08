package com.example.sistema_cla.domain.model;

import com.example.sistema_cla.domain.enums.StatusAcessibilidade;
import lombok.*;

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

    // Alterado para usar AvaliacaoComposite
    private AvaliacaoComposite avaliacoes = new AvaliacaoComposite(); // Agora é um composite

}
