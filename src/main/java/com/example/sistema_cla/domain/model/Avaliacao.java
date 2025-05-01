package com.example.sistema_cla.domain.model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Avaliacao {
    private Long id;
    private Usuario usuario;
    private Local localAcessivel;
    private int nota; // Escala de 1 a 5
    private String comentario;
    private LocalDate dataAvaliacao;

    /**
     * Cria um memento do estado atual da avaliação
     * @return Um objeto memento com o estado atual
     */
    public AvaliacaoMemento salvar() {
        return new AvaliacaoMemento(this);
    }

    /**
     * Restaura o estado da avaliação a partir de um memento
     * @param memento O estado a ser restaurado
     */
    public void editar(AvaliacaoMemento memento) {
        this.nota = memento.getNota();
        this.comentario = memento.getComentario();
        this.dataAvaliacao = memento.getDataAvaliacao();

        // Não restauramos o ID, pois é a identidade do objeto

        // Restaurando relacionamentos se necessário
        if (this.usuario == null && memento.getUsuarioId() != null) {
            this.usuario = new Usuario();
        }
        if (this.usuario != null) {
            this.usuario.setId(memento.getUsuarioId());
        }

        if (this.localAcessivel == null && memento.getLocalId() != null) {
            this.localAcessivel = new Local();
        }
        if (this.localAcessivel != null) {
            this.localAcessivel.setId(memento.getLocalId());
        }
    }

    /**
     * Edita a avaliação com novos valores
     * @param novoComentario Novo comentário
     * @param novaNota Nova nota
     */

    public void editar(String novoComentario, int novaNota) {
        this.comentario = novoComentario;
        this.nota = novaNota;
        this.dataAvaliacao = LocalDate.now();
    }
}