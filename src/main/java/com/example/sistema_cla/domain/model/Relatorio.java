package com.example.sistema_cla.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class Relatorio {
    private Long id;
    private String titulo;
    private LocalDate dataGeracao;
    private String conteudo;
    private String tipo; // "HTML" ou "PDF"
    private String categoria; // "ESTATISTICA_ACESSO", "AVALIACAO", etc.
    private Usuario usuario; // usuário que solicitou o relatório
    private LocalDate periodoInicio;
    private LocalDate periodoFim;


    public Relatorio(String titulo, String tipo) {
        this.titulo = titulo;
        this.tipo = tipo;
        this.dataGeracao = LocalDate.now();
    }

}