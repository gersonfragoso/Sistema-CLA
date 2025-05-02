package com.example.sistema_cla.domain.observer;

public interface AcessoSubject {
    void registrarObserver(AcessoObserver observer);
    void removerObserver(AcessoObserver observer);
    void notificarObservers(com.example.sistema_cla.domain.model.RegistroAcesso registroAcesso);
}