package com.example.sistema_cla.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Acesso {
    private String usuario;
    private String data;
    private String local;

    // Getters e setters omitidos por brevidade
}