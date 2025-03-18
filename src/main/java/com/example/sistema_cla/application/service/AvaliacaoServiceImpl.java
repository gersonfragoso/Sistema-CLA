package com.example.sistema_cla.application.service;

import com.example.sistema_cla.infrastructure.exceptions.LocalNotFoundException;
import com.example.sistema_cla.infrastructure.exceptions.UsuarioNotFoundException;
import com.example.sistema_cla.presentation.dto.request.AvaliacaoRequest;
import com.example.sistema_cla.presentation.dto.response.AvaliacaoResponse;
import com.example.sistema_cla.domain.model.Avaliacao;
import com.example.sistema_cla.domain.model.Local;
import com.example.sistema_cla.domain.model.Usuario;
import com.example.sistema_cla.domain.repository.AvaliacaoRepository;
import com.example.sistema_cla.domain.repository.LocalRepository;
import com.example.sistema_cla.domain.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AvaliacaoServiceImpl {
    private final AvaliacaoRepository avaliacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final LocalRepository localRepository;

    public AvaliacaoServiceImpl(AvaliacaoRepository avaliacaoRepository,
                                UsuarioRepository usuarioRepository, LocalRepository localRepository) {
        this.avaliacaoRepository = avaliacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.localRepository = localRepository;
    }

    public AvaliacaoResponse criarAvaliacao(AvaliacaoRequest request) {
        Usuario usuario = usuarioRepository.findById(request.usuarioId())
                .orElseThrow(() -> new UsuarioNotFoundException(request.usuarioId()));
        Local local = localRepository.findById(request.localId())
                .orElseThrow(() -> new LocalNotFoundException(request.localId()));

        Avaliacao avaliacao = new Avaliacao(null, usuario, local, request.nota(),
                request.comentario(), LocalDate.now());
        Avaliacao salvo = avaliacaoRepository.save(avaliacao);
        return new AvaliacaoResponse(
                salvo.getId(),
                salvo.getUsuario().getId(),
                salvo.getLocalAcessivel().getId(),
                salvo.getNota(),
                salvo.getComentario());
    }
}