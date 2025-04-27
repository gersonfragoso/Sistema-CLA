package com.example.sistema_cla.infrastructure.dao.impl;

import com.example.sistema_cla.infrastructure.dao.interfaces.GenericDAO;
import com.example.sistema_cla.infrastructure.exceptions.ConnectionException;
import com.example.sistema_cla.infrastructure.exceptions.DataIntegrityException;
import com.example.sistema_cla.infrastructure.exceptions.EntityNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.PersistenceException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

public abstract class JpaGenericDAO<T, ID> implements GenericDAO<T, ID> {

    @PersistenceContext
    protected EntityManager entityManager;

    protected Class<T> entityClass;

    public JpaGenericDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    @Transactional
    public T save(T entity) {
        try {
            if (entityManager.contains(entity)) {
                return entityManager.merge(entity);
            } else {
                entityManager.persist(entity);
                return entity;
            }
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Erro ao salvar entidade: violação de integridade de dados", e);
        } catch (PersistenceException e) {
            throw new ConnectionException("Erro de conexão ao tentar salvar entidade", e);
        }
    }

    @Override
    public Optional<T> findById(ID id) {
        try {
            T entity = entityManager.find(entityClass, id);
            return Optional.ofNullable(entity);
        } catch (PersistenceException e) {
            throw new ConnectionException("Erro de conexão ao buscar entidade com ID " + id, e);
        }
    }

    // Método adicional para buscar e lançar exceção se não encontrar
    public T getById(ID id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException(entityClass.getSimpleName(), (Long) id));
    }

    @Override
    public List<T> findAll() {
        try {
            return entityManager
                    .createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass)
                    .getResultList();
        } catch (PersistenceException e) {
            throw new ConnectionException("Erro de conexão ao buscar todas as entidades", e);
        }
    }

    @Override
    @Transactional
    public void delete(T entity) {
        try {
            entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível excluir esta entidade devido a restrições de integridade", e);
        } catch (PersistenceException e) {
            throw new ConnectionException("Erro de conexão ao excluir entidade", e);
        }
    }

    @Override
    @Transactional
    public void deleteById(ID id) {
        try {
            T entity = getById(id);
            delete(entity);
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (PersistenceException e) {
            throw new ConnectionException("Erro de conexão ao excluir entidade com ID " + id, e);
        }
    }
}