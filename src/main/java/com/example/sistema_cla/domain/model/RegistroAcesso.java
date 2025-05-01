package com.example.sistema_cla.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class RegistroAcesso {
    private Long id;
    private Usuario usuario;
    private LocalDateTime dataAcesso;
    private int tempoSessaoMinutos;
    private int paginasVisitadas;
    private String ipAcesso;
    private String dispositivo;

    public RegistroAcesso(Usuario usuario, LocalDateTime dataAcesso, int tempoSessaoMinutos,
                          int paginasVisitadas, String ipAcesso, String dispositivo) {
        this.usuario = usuario;
        this.dataAcesso = dataAcesso;
        this.tempoSessaoMinutos = tempoSessaoMinutos;
        this.paginasVisitadas = paginasVisitadas;
        this.ipAcesso = ipAcesso;
        this.dispositivo = dispositivo;
    }
}