package com.example.sistema_cla.domain.model;

import java.util.ArrayList;
import java.util.List;

public class AvaliacaoComposite implements AvaliacaoComponente {
    private List<AvaliacaoComponente> avaliacoes = new ArrayList<>();

    public void adicionarAvaliacao(AvaliacaoComponente avaliacao) {
        avaliacoes.add(avaliacao);
    }

    public void removerAvaliacao(AvaliacaoComponente avaliacao) {
        avaliacoes.remove(avaliacao);
    }

    @Override
    public int getNota() {
        return (int) avaliacoes.stream().mapToInt(AvaliacaoComponente::getNota).average().orElse(0);
    }

    @Override
    public String getComentario() {
        StringBuilder comentarios = new StringBuilder();
        for (AvaliacaoComponente avaliacao : avaliacoes) {
            comentarios.append(avaliacao.getComentario()).append("\n");
        }
        return comentarios.toString();
    }
}
