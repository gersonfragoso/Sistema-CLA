package com.example.sistema_cla.domain.repository;

import com.example.sistema_cla.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}
