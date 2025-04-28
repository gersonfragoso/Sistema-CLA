package com.example.sistema_cla.infrastructure.dao.impl;

import com.example.sistema_cla.domain.enums.StatusAcessibilidade;
import com.example.sistema_cla.domain.model.Local;
import com.example.sistema_cla.infrastructure.banco.DatabaseConnection;
import com.example.sistema_cla.infrastructure.dao.interfaces.LocalDAO;
import com.example.sistema_cla.infrastructure.exceptions.ConnectionException;
import com.example.sistema_cla.infrastructure.exceptions.InvalidStatusAcessibilidadeException;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

@Repository
public class JdbcLocalDAO extends JdbcGenericDAO<Local, Long> implements LocalDAO {

    @Override
    protected String getTableName() {
        return "local";
    }

    @Override
    protected String getIdColumnName() {
        return "id";
    }

    @Override
    protected Local mapRowToEntity(ResultSet rs) throws SQLException {
        Local local = new Local();
        local.setId(rs.getLong("id"));
        local.setNome(rs.getString("nome"));
        local.setEndereco(rs.getString("endereco"));
        local.setTipoLocal(rs.getString("tipo_local"));

        // Converter String para enum
        String statusStr = rs.getString("status_acessibilidade");
        if (statusStr != null) {
            local.setStatusAcessibilidade(StatusAcessibilidade.valueOf(statusStr));
        }

        // Avaliacoes são geralmente carregadas separadamente

        return local;
    }

    @Override
    protected PreparedStatement prepareInsertStatement(Connection conn, Local entity) throws SQLException {
        String sql = "INSERT INTO local (nome, endereco, tipo_local, status_acessibilidade) " +
                "VALUES (?, ?, ?, ?) RETURNING id";

        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, entity.getNome());
        stmt.setString(2, entity.getEndereco());
        stmt.setString(3, entity.getTipoLocal());

        if (entity.getStatusAcessibilidade() != null) {
            stmt.setString(4, entity.getStatusAcessibilidade().name());
        } else {
            stmt.setNull(4, Types.VARCHAR);
        }

        return stmt;
    }

    @Override
    protected PreparedStatement prepareUpdateStatement(Connection conn, Local entity) throws SQLException {
        String sql = "UPDATE local SET nome = ?, endereco = ?, tipo_local = ?, " +
                "status_acessibilidade = ? WHERE id = ?";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, entity.getNome());
        stmt.setString(2, entity.getEndereco());
        stmt.setString(3, entity.getTipoLocal());

        if (entity.getStatusAcessibilidade() != null) {
            stmt.setString(4, entity.getStatusAcessibilidade().name());
        } else {
            stmt.setNull(4, Types.VARCHAR);
        }

        stmt.setLong(5, entity.getId());

        return stmt;
    }

    @Override
    protected boolean isNewEntity(Local entity) {
        return entity.getId() == null;
    }

    @Override
    protected Local getUpdatedEntityWithId(Local entity, ResultSet rs) throws SQLException {
        entity.setId(rs.getLong(1));
        return entity;
    }

    @Override
    protected Long getEntityId(Local entity) {
        return entity.getId();
    }

    @Override
    protected void setIdParameter(PreparedStatement stmt, Long id) throws SQLException {
        stmt.setLong(1, id);
    }

    @Override
    public List<Local> findByTipoLocal(String tipoLocal) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM local WHERE tipo_local = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, tipoLocal);

            rs = stmt.executeQuery();

            List<Local> result = new ArrayList<>();
            while (rs.next()) {
                result.add(mapRowToEntity(rs));
            }
            return result;

        } catch (SQLException e) {
            throw new ConnectionException("Erro ao buscar locais por tipo: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.closeResources(null, stmt, rs);
        }
    }

    @Override
    public List<Local> findByStatusAcessibilidade(StatusAcessibilidade status) {
        if (status == null) {
            throw new InvalidStatusAcessibilidadeException(status);
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM local WHERE status_acessibilidade = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, status.name());

            rs = stmt.executeQuery();

            List<Local> result = new ArrayList<>();
            while (rs.next()) {
                result.add(mapRowToEntity(rs));
            }
            return result;

        } catch (SQLException e) {
            throw new ConnectionException("Erro ao buscar locais por status: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.closeResources(null, stmt, rs);
        }
    }
}