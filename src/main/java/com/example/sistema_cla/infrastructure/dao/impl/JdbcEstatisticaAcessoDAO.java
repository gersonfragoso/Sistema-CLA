package com.example.sistema_cla.infrastructure.dao.impl;

import com.example.sistema_cla.domain.model.EstatisticaAcesso;
import com.example.sistema_cla.infrastructure.banco.DatabaseConnection;
import com.example.sistema_cla.infrastructure.dao.interfaces.EstatisticaAcessoDAO;
import com.example.sistema_cla.infrastructure.exceptions.ConnectionException;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcEstatisticaAcessoDAO extends JdbcGenericDAO<EstatisticaAcesso, Long> implements EstatisticaAcessoDAO {

    @Override
    protected String getTableName() {
        return "estatistica_acesso";
    }

    @Override
    protected String getIdColumnName() {
        return "usuario_id";
    }

    @Override
    protected EstatisticaAcesso mapRowToEntity(ResultSet rs) throws SQLException {
        EstatisticaAcesso estatistica = new EstatisticaAcesso();
        estatistica.setUsuarioId(rs.getLong("usuario_id"));
        estatistica.setNomeUsuario(rs.getString("nome_usuario"));
        estatistica.setQuantidadeAcessos(rs.getInt("quantidade_acessos"));

        Timestamp ultimoAcesso = rs.getTimestamp("ultimo_acesso");
        if (ultimoAcesso != null) {
            estatistica.setUltimoAcesso(ultimoAcesso.toLocalDateTime());
        }

        estatistica.setTempoTotalMinutos(rs.getLong("tempo_total_minutos"));
        estatistica.setPaginasVisitadas(rs.getInt("paginas_visitadas"));

        return estatistica;
    }

    @Override
    protected PreparedStatement prepareInsertStatement(Connection conn, EstatisticaAcesso entity) throws SQLException {
        String sql = "INSERT INTO estatistica_acesso (usuario_id, nome_usuario, quantidade_acessos, " +
                "ultimo_acesso, tempo_total_minutos, paginas_visitadas) VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setLong(1, entity.getUsuarioId());
        stmt.setString(2, entity.getNomeUsuario());
        stmt.setInt(3, entity.getQuantidadeAcessos());

        if (entity.getUltimoAcesso() != null) {
            stmt.setTimestamp(4, Timestamp.valueOf(entity.getUltimoAcesso()));
        } else {
            stmt.setNull(4, Types.TIMESTAMP);
        }

        stmt.setLong(5, entity.getTempoTotalMinutos());
        stmt.setInt(6, entity.getPaginasVisitadas());

        return stmt;
    }

    @Override
    protected PreparedStatement prepareUpdateStatement(Connection conn, EstatisticaAcesso entity) throws SQLException {
        String sql = "UPDATE estatistica_acesso SET nome_usuario = ?, quantidade_acessos = ?, " +
                "ultimo_acesso = ?, tempo_total_minutos = ?, paginas_visitadas = ? " +
                "WHERE usuario_id = ?";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, entity.getNomeUsuario());
        stmt.setInt(2, entity.getQuantidadeAcessos());

        if (entity.getUltimoAcesso() != null) {
            stmt.setTimestamp(3, Timestamp.valueOf(entity.getUltimoAcesso()));
        } else {
            stmt.setNull(3, Types.TIMESTAMP);
        }

        stmt.setLong(4, entity.getTempoTotalMinutos());
        stmt.setInt(5, entity.getPaginasVisitadas());
        stmt.setLong(6, entity.getUsuarioId());

        return stmt;
    }

    @Override
    protected boolean isNewEntity(EstatisticaAcesso entity) {
        // Como usuarioId é a chave primária, precisamos verificar se já existe
        return !findByUsuarioId(entity.getUsuarioId()).isPresent();
    }

    @Override
    protected EstatisticaAcesso getUpdatedEntityWithId(EstatisticaAcesso entity, ResultSet rs) throws SQLException {
        // Não precisa atualizar o ID pois já está definido
        return entity;
    }

    @Override
    protected Long getEntityId(EstatisticaAcesso entity) {
        return entity.getUsuarioId();
    }

    @Override
    protected void setIdParameter(PreparedStatement stmt, Long id) throws SQLException {
        stmt.setLong(1, id);
    }

    @Override
    public Optional<EstatisticaAcesso> findByUsuarioId(Long usuarioId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM estatistica_acesso WHERE usuario_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, usuarioId);

            rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapRowToEntity(rs));
            }
            return Optional.empty();

        } catch (SQLException e) {
            throw new ConnectionException("Erro ao buscar estatística por usuário ID: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.closeResources(null, stmt, rs);
        }
    }

    @Override
    public EstatisticaAcesso update(EstatisticaAcesso estatisticaAcesso) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = prepareUpdateStatement(conn, estatisticaAcesso);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                // Se não atualizou nenhuma linha, insere um novo registro
                stmt.close();
                stmt = prepareInsertStatement(conn, estatisticaAcesso);
                stmt.executeUpdate();
            }

            return estatisticaAcesso;

        } catch (SQLException e) {
            throw new ConnectionException("Erro ao atualizar estatística de acesso: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.closeResources(null, stmt, null);
        }
    }

    @Override
    public List<EstatisticaAcesso> findTopUsuariosAtivos(int limit) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM estatistica_acesso ORDER BY quantidade_acessos DESC LIMIT ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, limit);

            rs = stmt.executeQuery();

            List<EstatisticaAcesso> result = new ArrayList<>();
            while (rs.next()) {
                result.add(mapRowToEntity(rs));
            }
            return result;

        } catch (SQLException e) {
            throw new ConnectionException("Erro ao buscar usuários mais ativos: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.closeResources(null, stmt, rs);
        }
    }
}