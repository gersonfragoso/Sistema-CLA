package com.example.sistema_cla.domain.observer;

import com.example.sistema_cla.domain.model.RegistroAcesso;

public interface AcessoObserver {
    void onNovoAcesso(RegistroAcesso registroAcesso);
}