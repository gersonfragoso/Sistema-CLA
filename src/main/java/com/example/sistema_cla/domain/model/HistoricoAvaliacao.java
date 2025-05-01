package com.example.sistema_cla.domain.model;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Classe responsável por gerenciar o histórico de estados de avaliações (Caretaker)
 */
public class HistoricoAvaliacao {
    private final Deque<AvaliacaoMemento> estados = new ArrayDeque<>();
    private static final int MAX_ESTADOS = 10; // Limite de estados salvos

    /**
     * Salva um estado da avaliação
     * @param memento O estado a ser salvo
     */

    public void salvarEstado(AvaliacaoMemento memento) {
        // Se atingiu o limite, remove o estado mais antigo
        if (estados.size() >= MAX_ESTADOS) {
            estados.removeLast();
        }
        estados.push(memento);
    }

    /**
     * Recupera o estado anterior da avaliação
     * @return O estado anterior ou null se não houver
     */
    public AvaliacaoMemento desfazer() {
        if (estados.isEmpty()) {
            return null;
        }
        return estados.pop();
    }

    /**
     * Verifica se há estados para desfazer
     * @return true se houver estados salvos
     */

    public boolean temEstadosParaDesfazer() {
        return !estados.isEmpty();
    }

    /**
     * Limpa todos os estados salvos
     */

    public void limparHistorico() {
        estados.clear();
    }
}