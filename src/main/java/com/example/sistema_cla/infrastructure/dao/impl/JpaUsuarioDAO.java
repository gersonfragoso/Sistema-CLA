package com.example.sistema_cla.infrastructure.dao.impl;

import com.example.sistema_cla.domain.model.Usuario;
import com.example.sistema_cla.infrastructure.dao.interfaces.UsuarioDAO;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaUsuarioDAO extends JpaGenericDAO<Usuario, Long> implements UsuarioDAO {

    public JpaUsuarioDAO() {
        super(Usuario.class);
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        try {
            Usuario usuario = entityManager
                    .createQuery("SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return Optional.of(usuario);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Usuario> findByCpf(String cpf) {
        try {
            Usuario usuario = entityManager
                    .createQuery("SELECT u FROM Usuario u WHERE u.cpf = :cpf", Usuario.class)
                    .setParameter("cpf", cpf)
                    .getSingleResult();
            return Optional.of(usuario);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}

