package com.example.sistema_cla.domain.observer.impl;


import com.example.sistema_cla.domain.model.RegistroAcesso;
import com.example.sistema_cla.domain.observer.AcessoObserver;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class LogAcessoObserver implements AcessoObserver {
    @Override
    public void onNovoAcesso(RegistroAcesso registroAcesso) {
        // Registrar log de acesso
        System.out.println("LOG: Novo acesso registrado: " +
                "Usuário: " + registroAcesso.getUsuario().getNomeCompleto() +
                " | Data: " + registroAcesso.getDataAcesso().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) +
                " | IP: " + registroAcesso.getIpAcesso() +
                " | Dispositivo: " + registroAcesso.getDispositivo());
    }
}