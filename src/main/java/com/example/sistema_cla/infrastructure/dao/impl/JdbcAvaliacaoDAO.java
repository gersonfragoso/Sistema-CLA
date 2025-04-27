package com.example.sistema_cla.infrastructure.dao.impl;

import com.example.sistema_cla.domain.model.Avaliacao;
import com.example.sistema_cla.domain.model.Local;
import com.example.sistema_cla.domain.model.Usuario;
import com.example.sistema_cla.infrastructure.banco.DatabaseConnection;
import com.example.sistema_cla.infrastructure.dao.interfaces.AvaliacaoDAO;
import com.example.sistema_cla.infrastructure.exceptions.ConnectionException;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

@Repository
public class JdbcAvaliacaoDAO extends JdbcGenericDAO<Avaliacao, Long> implements AvaliacaoDAO {

    @Override
    protected String getTableName() {
        return "avaliacao";
    }

    @Override
    protected String getIdColumnName() {
        return "id";
    }

    @Override
    protected Avaliacao mapRowToEntity(ResultSet rs) throws SQLException {
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setId(rs.getLong("id"));
        avaliacao.setNota(rs.getInt("nota"));
        avaliacao.setComentario(rs.getString("comentario"));

        // Converter para LocalDate
        Date dataAvaliacao = rs.getDate("data_avaliacao");
        if (dataAvaliacao != null) {
            avaliacao.setDataAvaliacao(dataAvaliacao.toLocalDate());
        }

        // Para relacionamentos, geralmente fazemos consultas adicionais
        // ou apenas armazenamos os IDs e carregamos quando necessário
        Long usuarioId = rs.getLong("usuario_id");
        if (!rs.wasNull()) {
            Usuario usuario = new Usuario();
            usuario.setId(usuarioId);
            avaliacao.setUsuario(usuario);
        }

        Long localId = rs.getLong("local_id");
        if (!rs.wasNull()) {
            Local local = new Local();
            local.setId(localId);
            avaliacao.setLocalAcessivel(local);
        }

        return avaliacao;
    }

    @Override
    protected PreparedStatement prepareInsertStatement(Connection conn, Avaliacao entity) throws SQLException {
        String sql = "INSERT INTO avaliacao (usuario_id, local_id, nota, comentario, data_avaliacao) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING id";

        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        if (entity.getUsuario() != null && entity.getUsuario().getId() != null) {
            stmt.setLong(1, entity.getUsuario().getId());
        } else {
            stmt.setNull(1, Types.BIGINT);
        }

        if (entity.getLocalAcessivel() != null && entity.getLocalAcessivel().getId() != null) {
            stmt.setLong(2, entity.getLocalAcessivel().getId());
        } else {
            stmt.setNull(2, Types.BIGINT);
        }

        stmt.setInt(3, entity.getNota());
        stmt.setString(4, entity.getComentario());

        if (entity.getDataAvaliacao() != null) {
            stmt.setDate(5, Date.valueOf(entity.getDataAvaliacao()));
        } else {
            stmt.setNull(5, Types.DATE);
        }

        return stmt;
    }

    @Override
    protected PreparedStatement prepareUpdateStatement(Connection conn, Avaliacao entity) throws SQLException {
        String sql = "UPDATE avaliacao SET usuario_id = ?, local_id = ?, nota = ?, " +
                "comentario = ?, data_avaliacao = ? WHERE id = ?";

        PreparedStatement stmt = conn.prepareStatement(sql);

        if (entity.getUsuario() != null && entity.getUsuario().getId() != null) {
            stmt.setLong(1, entity.getUsuario().getId());
        } else {
            stmt.setNull(1, Types.BIGINT);
        }

        if (entity.getLocalAcessivel() != null && entity.getLocalAcessivel().getId() != null) {
            stmt.setLong(2, entity.getLocalAcessivel().getId());
        } else {
            stmt.setNull(2, Types.BIGINT);
        }

        stmt.setInt(3, entity.getNota());
        stmt.setString(4, entity.getComentario());

        if (entity.getDataAvaliacao() != null) {
            stmt.setDate(5, Date.valueOf(entity.getDataAvaliacao()));
        } else {
            stmt.setNull(5, Types.DATE);
        }

        stmt.setLong(6, entity.getId());

        return stmt;
    }

    @Override
    protected boolean isNewEntity(Avaliacao entity) {
        return entity.getId() == null;
    }

    @Override
    protected Avaliacao getUpdatedEntityWithId(Avaliacao entity, ResultSet rs) throws SQLException {
        entity.setId(rs.getLong(1));
        return entity;
    }

    @Override
    protected Long getEntityId(Avaliacao entity) {
        return entity.getId();
    }

    @Override
    protected void setIdParameter(PreparedStatement stmt, Long id) throws SQLException {
        stmt.setLong(1, id);
    }

    @Override
    public List<Avaliacao> findByUsuarioId(Long usuarioId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM avaliacao WHERE usuario_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, usuarioId);

            rs = stmt.executeQuery();

            List<Avaliacao> result = new ArrayList<>();
            while (rs.next()) {
                result.add(mapRowToEntity(rs));
            }
            return result;

        } catch (SQLException e) {
            throw new ConnectionException("Erro ao buscar avaliações por usuário ID: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.closeResources(null, stmt, rs);
        }
    }

    @Override
    public List<Avaliacao> findByLocalId(Long localId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM avaliacao WHERE local_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, localId);

            rs = stmt.executeQuery();

            List<Avaliacao> result = new ArrayList<>();
            while (rs.next()) {
                result.add(mapRowToEntity(rs));
            }
            return result;

        } catch (SQLException e) {
            throw new ConnectionException("Erro ao buscar avaliações por local ID: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.closeResources(null, stmt, rs);
        }
    }

    @Override
    public List<Avaliacao> findByLocalAcessivel(Local local) {
        if (local == null || local.getId() == null) {
            throw new IllegalArgumentException("Local ou ID do local não pode ser nulo");
        }
        return findByLocalId(local.getId());
    }
}