package com.example.sistema_cla.adapter.outbound.repositories;

import com.example.sistema_cla.adapter.outbound.entities.JpaUsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaUsuarioRepository extends JpaRepository<JpaUsuarioEntity,Long> {

    Optional<JpaUsuarioEntity> findByCpf(String cpf);
    @Query("SELECT u FROM JpaUsuarioEntity u JOIN u.telefone t WHERE t.ddd = :ddd AND t.numeroTelefone = :numeroTelefone")
    Optional<JpaUsuarioEntity> findByTelefoneDddAndNumero(String ddd, String numeroTelefone);

}
