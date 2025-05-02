package com.example.sistema_cla.application.service;

import com.example.sistema_cla.domain.model.RegistroAcesso;
import com.example.sistema_cla.domain.model.Usuario;
import com.example.sistema_cla.domain.observer.AcessoObserver;
import com.example.sistema_cla.domain.observer.AcessoSubject;
import com.example.sistema_cla.infrastructure.dao.interfaces.RegistroAcessoDAO;
import com.example.sistema_cla.infrastructure.dao.interfaces.UsuarioDAO;
import com.example.sistema_cla.infrastructure.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RegistroAcessoService implements AcessoSubject {
    private final RegistroAcessoDAO registroAcessoDAO;
    private final UsuarioDAO usuarioDAO;
    private final List<AcessoObserver> observers = new ArrayList<>();

    public RegistroAcessoService(
            RegistroAcessoDAO registroAcessoDAO,
            UsuarioDAO usuarioDAO,
            List<AcessoObserver> observers) {
        this.registroAcessoDAO = registroAcessoDAO;
        this.usuarioDAO = usuarioDAO;

        // Registrar observadores iniciais
        if (observers != null) {
            this.observers.addAll(observers);
        }
    }

    @Override
    public void registrarObserver(AcessoObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removerObserver(AcessoObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notificarObservers(RegistroAcesso registroAcesso) {
        for (AcessoObserver observer : observers) {
            observer.onNovoAcesso(registroAcesso);
        }
    }

    // Método para registrar um novo acesso
    public RegistroAcesso registrarAcesso(
            Long usuarioId,
            String ipAcesso,
            String dispositivo,
            int tempoSessaoMinutos,
            int paginasVisitadas) {

        Usuario usuario = usuarioDAO.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário", usuarioId));

        RegistroAcesso registroAcesso = new RegistroAcesso(
                usuario,
                LocalDateTime.now(),
                tempoSessaoMinutos,
                paginasVisitadas,
                ipAcesso,
                dispositivo
        );

        // Salvar no banco de dados
        RegistroAcesso registroSalvo = registroAcessoDAO.save(registroAcesso);

        // Notificar todos os observadores
        notificarObservers(registroSalvo);

        return registroSalvo;
    }

    // Método para buscar registros de acesso de um usuário
    public List<RegistroAcesso> buscarRegistrosPorUsuario(Long usuarioId) {
        return registroAcessoDAO.findByUsuarioId(usuarioId);
    }

    // Método para buscar registros de acesso por período
    public List<RegistroAcesso> buscarRegistrosPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return registroAcessoDAO.findByDataAcessoBetween(inicio, fim);
    }

    // Método para buscar registros de acesso de um usuário em um período
    public List<RegistroAcesso> buscarRegistrosPorUsuarioEPeriodo(
            Long usuarioId, LocalDateTime inicio, LocalDateTime fim) {
        return registroAcessoDAO.findByUsuarioIdAndDataAcessoBetween(usuarioId, inicio, fim);
    }
}