package com.example.sistema_cla.domain.observer.impl;


import com.example.sistema_cla.domain.model.RegistroAcesso;
import com.example.sistema_cla.domain.model.Usuario;
import com.example.sistema_cla.domain.observer.AcessoObserver;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

//Essas classes não tem como testar por que teria que subir o serviço para poder povoar ele, e a implementação disse
// deixario o codigo muito mais complexo de implementar.
@Component
public class SegurancaAcessoObserver implements AcessoObserver {
    // Mapa para controlar a quantidade de acessos por usuário em um curto período
    private final Map<Long, Integer> acessosRecentes = new HashMap<>();
    private final Map<Long, LocalDateTime> ultimoAlerta = new HashMap<>();

    // Limite de acessos em curto período para gerar alerta
    private static final int LIMITE_ACESSOS = 10;
    private static final int JANELA_TEMPO_MINUTOS = 5;

    @Override
    public void onNovoAcesso(RegistroAcesso registroAcesso) {
        Usuario usuario = registroAcesso.getUsuario();
        Long usuarioId = usuario.getId();

        // Atualizar contagem de acessos recentes
        int acessos = acessosRecentes.getOrDefault(usuarioId, 0) + 1;
        acessosRecentes.put(usuarioId, acessos);

        // Verificar se excedeu limite
        if (acessos >= LIMITE_ACESSOS) {
            // Verificar se já emitiu alerta recentemente
            LocalDateTime ultimoAlertaTime = ultimoAlerta.getOrDefault(usuarioId, LocalDateTime.MIN);
            if (ultimoAlertaTime.plusMinutes(JANELA_TEMPO_MINUTOS).isBefore(LocalDateTime.now())) {
                // Emitir alerta de segurança
                System.out.println("ALERTA DE SEGURANÇA: Muitos acessos detectados para o usuário " +
                        usuario.getNomeCompleto() + " (" + acessos + " acessos em curto período)");

                // Registrar alerta
                ultimoAlerta.put(usuarioId, LocalDateTime.now());

                // Limpar contador
                acessosRecentes.put(usuarioId, 0);
            }
        }

        // Limpar contadores antigos periodicamente
        // Na prática, isso seria feito usando um scheduler
    }
}