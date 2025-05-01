package com.example.sistema_cla.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EstatisticaAcesso {
    private Long usuarioId;
    private String nomeUsuario;
    private int quantidadeAcessos;
    private LocalDateTime ultimoAcesso;
    private long tempoTotalMinutos;
    private int paginasVisitadas;
}