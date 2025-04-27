package com.example.sistema_cla.application.service;

import com.example.sistema_cla.domain.model.Avaliacao;
import com.example.sistema_cla.domain.model.Local;
import com.example.sistema_cla.domain.model.Usuario;
import com.example.sistema_cla.infrastructure.dao.interfaces.AvaliacaoDAO;
import com.example.sistema_cla.infrastructure.dao.interfaces.LocalDAO;
import com.example.sistema_cla.infrastructure.dao.interfaces.UsuarioDAO;
import com.example.sistema_cla.infrastructure.exceptions.EntidadeNaoEncontradaException;
import com.example.sistema_cla.infrastructure.utils.AvaliacaoMapper;
import com.example.sistema_cla.presentation.dto.response.AvaliacaoResponse;
import com.example.sistema_cla.presentation.dto.response.LocalComAvaliacoesResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BuscarEntidadeService {
    private final UsuarioDAO usuarioDAO;
    private final LocalDAO localDAO;
    private final AvaliacaoDAO avaliacaoDAO;


    public BuscarEntidadeService(UsuarioDAO usuarioDAO, LocalDAO localDAO, AvaliacaoDAO avaliacaoDAO) {
        this.usuarioDAO = usuarioDAO;
        this.localDAO = localDAO;
        this.avaliacaoDAO = avaliacaoDAO;
    }

    public Usuario buscarUsuario(Long id) {
        return usuarioDAO.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário", id));
    }

    public Local buscarLocal(Long id) {
        return localDAO.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Local", id));
    }
    public LocalComAvaliacoesResponse buscarLocalComAvaliacoes(Long id) {
        Local local = buscarLocal(id);
        List<Avaliacao> avaliacoes = avaliacaoDAO.findByLocalAcessivel(local);

        double mediaAvaliacoes = calcularMediaAvaliacoes(avaliacoes);
        List<AvaliacaoResponse> avaliacoesResponse = avaliacoes.stream()
                .map(AvaliacaoMapper::toResponse)
                .collect(Collectors.toList());

        return new LocalComAvaliacoesResponse(
                local.getId(),
                local.getNome(),
                local.getEndereco(),
                local.getStatusAcessibilidade().name(),
                mediaAvaliacoes,
                avaliacoesResponse
        );
    }

    private double calcularMediaAvaliacoes(List<Avaliacao> avaliacoes) {
        if (avaliacoes.isEmpty()) {
            return 0.0;
        }
        return avaliacoes.stream()
                .mapToInt(Avaliacao::getNota)
                .average()
                .orElse(0.0);
    }
}