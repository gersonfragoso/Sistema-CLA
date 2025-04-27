package com.example.sistema_cla.infrastructure.dao.impl;


import com.example.sistema_cla.infrastructure.banco.DatabaseConnection;
import com.example.sistema_cla.infrastructure.dao.interfaces.GenericDAO;
import com.example.sistema_cla.infrastructure.exceptions.ConnectionException;
import com.example.sistema_cla.infrastructure.exceptions.DataIntegrityException;
import com.example.sistema_cla.infrastructure.exceptions.EntityNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class JdbcGenericDAO<T, ID> implements GenericDAO<T, ID> {

    protected abstract String getTableName();
    protected abstract String getIdColumnName();
    protected abstract T mapRowToEntity(ResultSet rs) throws SQLException;
    protected abstract PreparedStatement prepareInsertStatement(Connection conn, T entity) throws SQLException;
    protected abstract PreparedStatement prepareUpdateStatement(Connection conn, T entity) throws SQLException;

    @Override
    public T save(T entity) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();

            if (isNewEntity(entity)) {
                // Insert
                stmt = prepareInsertStatement(conn, entity);
                stmt.executeUpdate();

                // Obter ID gerado se necessário
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    return getUpdatedEntityWithId(entity, rs);
                }
                return entity;
            } else {
                // Update
                stmt = prepareUpdateStatement(conn, entity);
                stmt.executeUpdate();
                return entity;
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("violates foreign key constraint") ||
                    e.getMessage().contains("violates unique constraint")) {
                throw new DataIntegrityException("Erro de integridade de dados ao salvar entidade: " + e.getMessage(), e);
            } else {
                throw new ConnectionException("Erro ao salvar entidade: " + e.getMessage(), e);
            }
        } finally {
            DatabaseConnection.closeResources(null, stmt, rs);
        }
    }

    @Override
    public Optional<T> findById(ID id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM " + getTableName() + " WHERE " + getIdColumnName() + " = ?";
            stmt = conn.prepareStatement(sql);
            setIdParameter(stmt, id);

            rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapRowToEntity(rs));
            }
            return Optional.empty();

        } catch (SQLException e) {
            throw new ConnectionException("Erro ao buscar entidade por ID: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.closeResources(null, stmt, rs);
        }
    }

    public T getById(ID id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException(getTableName(), (Long) id));
    }

    @Override
    public List<T> findAll() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM " + getTableName();
            stmt = conn.prepareStatement(sql);

            rs = stmt.executeQuery();

            List<T> result = new ArrayList<>();
            while (rs.next()) {
                result.add(mapRowToEntity(rs));
            }
            return result;

        } catch (SQLException e) {
            throw new ConnectionException("Erro ao buscar todas as entidades: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.closeResources(null, stmt, rs);
        }
    }

    @Override
    public void delete(T entity) {
        deleteById(getEntityId(entity));
    }

    @Override
    public void deleteById(ID id) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            String sql = "DELETE FROM " + getTableName() + " WHERE " + getIdColumnName() + " = ?";
            stmt = conn.prepareStatement(sql);
            setIdParameter(stmt, id);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new EntityNotFoundException(getTableName(), (Long) id);
            }

        } catch (SQLException e) {
            if (e.getMessage().contains("violates foreign key constraint")) {
                throw new DataIntegrityException("Não é possível excluir esta entidade devido a restrições de integridade", e);
            } else {
                throw new ConnectionException("Erro ao excluir entidade: " + e.getMessage(), e);
            }
        } finally {
            DatabaseConnection.closeResources(null, stmt, null);
        }
    }

    // Métodos abstratos adicionais que devem ser implementados pelas subclasses
    protected abstract boolean isNewEntity(T entity);
    protected abstract T getUpdatedEntityWithId(T entity, ResultSet rs) throws SQLException;
    protected abstract ID getEntityId(T entity);
    protected abstract void setIdParameter(PreparedStatement stmt, ID id) throws SQLException;
}