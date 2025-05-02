package com.example.sistema_cla.domain.command;

import org.springframework.stereotype.Component;
import java.util.Stack;

@Component
public class CommandInvoker {
    private final Stack<AvaliacaoCommand> commandsExecutados = new Stack<>();

    public void executarComando(AvaliacaoCommand command) {
        command.execute();
        commandsExecutados.push(command);
    }

    public void desfazerUltimoComando() {
        if (!commandsExecutados.isEmpty()) {
            AvaliacaoCommand ultimoComando = commandsExecutados.pop();
            ultimoComando.undo();
        }
    }
}
