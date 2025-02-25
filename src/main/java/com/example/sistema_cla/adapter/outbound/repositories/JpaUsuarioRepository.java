package com.example.sistema_cla.adapter.outbound.repositories;

import com.example.sistema_cla.adapter.outbound.entities.JpaUsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUsuarioRepository extends JpaRepository<JpaUsuarioEntity,Long> {
}
