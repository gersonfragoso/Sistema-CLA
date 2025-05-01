package com.example.sistema_cla.infrastructure.dao.impl;

import com.example.sistema_cla.domain.model.Relatorio;
import com.example.sistema_cla.domain.model.Usuario;
import com.example.sistema_cla.infrastructure.banco.DatabaseConnection;
import com.example.sistema_cla.infrastructure.dao.interfaces.RelatorioDAO;
import com.example.sistema_cla.infrastructure.exceptions.ConnectionException;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcRelatorioDAO extends JdbcGenericDAO<Relatorio, Long> implements RelatorioDAO {

    @Override
    protected String getTableName() {
        return "relatorio";
    }

    @Override
    protected String getIdColumnName() {
        return "id";
    }

    @Override
    protected Relatorio mapRowToEntity(ResultSet rs) throws SQLException {
        Relatorio relatorio = new Relatorio();
        relatorio.setId(rs.getLong("id"));
        relatorio.setTitulo(rs.getString("titulo"));

        // Converter para LocalDate
        Date dataGeracao = rs.getDate("data_geracao");
        if (dataGeracao != null) {
            relatorio.setDataGeracao(dataGeracao.toLocalDate());
        }

        relatorio.setConteudo(rs.getString("conteudo"));
        relatorio.setTipo(rs.getString("tipo"));
        relatorio.setCategoria(rs.getString("categoria"));

        // Datas de período
        Date periodoInicio = rs.getDate("periodo_inicio");
        if (periodoInicio != null) {
            relatorio.setPeriodoInicio(periodoInicio.toLocalDate());
        }

        Date periodoFim = rs.getDate("periodo_fim");
        if (periodoFim != null) {
            relatorio.setPeriodoFim(periodoFim.toLocalDate());
        }

        // Para relacionamentos, apenas armazenamos os IDs
        Long usuarioId = rs.getLong("usuario_id");
        if (!rs.wasNull()) {
            Usuario usuario = new Usuario();
            usuario.setId(usuarioId);
            relatorio.setUsuario(usuario);
        }

        return relatorio;
    }

    @Override
    protected PreparedStatement prepareInsertStatement(Connection conn, Relatorio entity) throws SQLException {
        String sql = "INSERT INTO relatorio (titulo, data_geracao, conteudo, tipo, categoria, " +
                "periodo_inicio, periodo_fim, usuario_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, entity.getTitulo());

        if (entity.getDataGeracao() != null) {
            stmt.setDate(2, Date.valueOf(entity.getDataGeracao()));
        } else {
            stmt.setDate(2, Date.valueOf(LocalDate.now()));
        }

        stmt.setString(3, entity.getConteudo());
        stmt.setString(4, entity.getTipo());

        if (entity.getCategoria() != null) {
            stmt.setString(5, entity.getCategoria());
        } else {
            stmt.setNull(5, Types.VARCHAR);
        }

        if (entity.getPeriodoInicio() != null) {
            stmt.setDate(6, Date.valueOf(entity.getPeriodoInicio()));
        } else {
            stmt.setNull(6, Types.DATE);
        }

        if (entity.getPeriodoFim() != null) {
            stmt.setDate(7, Date.valueOf(entity.getPeriodoFim()));
        } else {
            stmt.setNull(7, Types.DATE);
        }

        if (entity.getUsuario() != null && entity.getUsuario().getId() != null) {
            stmt.setLong(8, entity.getUsuario().getId());
        } else {
            stmt.setNull(8, Types.BIGINT);
        }

        return stmt;
    }

    @Override
    protected PreparedStatement prepareUpdateStatement(Connection conn, Relatorio entity) throws SQLException {
        String sql = "UPDATE relatorio SET titulo = ?, data_geracao = ?, conteudo = ?, " +
                "tipo = ?, categoria = ?, periodo_inicio = ?, periodo_fim = ?, usuario_id = ? WHERE id = ?";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, entity.getTitulo());

        if (entity.getDataGeracao() != null) {
            stmt.setDate(2, Date.valueOf(entity.getDataGeracao()));
        } else {
            stmt.setNull(2, Types.DATE);
        }

        stmt.setString(3, entity.getConteudo());
        stmt.setString(4, entity.getTipo());

        if (entity.getCategoria() != null) {
            stmt.setString(5, entity.getCategoria());
        } else {
            stmt.setNull(5, Types.VARCHAR);
        }

        if (entity.getPeriodoInicio() != null) {
            stmt.setDate(6, Date.valueOf(entity.getPeriodoInicio()));
        } else {
            stmt.setNull(6, Types.DATE);
        }

        if (entity.getPeriodoFim() != null) {
            stmt.setDate(7, Date.valueOf(entity.getPeriodoFim()));
        } else {
            stmt.setNull(7, Types.DATE);
        }

        if (entity.getUsuario() != null && entity.getUsuario().getId() != null) {
            stmt.setLong(8, entity.getUsuario().getId());
        } else {
            stmt.setNull(8, Types.BIGINT);
        }

        stmt.setLong(9, entity.getId());

        return stmt;
    }

    // Implementação dos métodos abstratos que faltavam da classe JdbcGenericDAO
    @Override
    protected boolean isNewEntity(Relatorio entity) {
        return entity.getId() == null;
    }

    @Override
    protected Relatorio getUpdatedEntityWithId(Relatorio entity, ResultSet rs) throws SQLException {
        entity.setId(rs.getLong(1));
        return entity;
    }

    @Override
    protected Long getEntityId(Relatorio entity) {
        return entity.getId();
    }

    @Override
    protected void setIdParameter(PreparedStatement stmt, Long id) throws SQLException {
        stmt.setLong(1, id);
    }

    // Implementações dos métodos da interface RelatorioDAO
    @Override
    public List<Relatorio> findByUsuarioId(Long usuarioId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM relatorio WHERE usuario_id = ? ORDER BY data_geracao DESC";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, usuarioId);

            rs = stmt.executeQuery();

            List<Relatorio> result = new ArrayList<>();
            while (rs.next()) {
                result.add(mapRowToEntity(rs));
            }
            return result;

        } catch (SQLException e) {
            throw new ConnectionException("Erro ao buscar relatórios por usuário ID: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.closeResources(null, stmt, rs);
        }
    }

    @Override
    public List<Relatorio> findByTipo(String tipo) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM relatorio WHERE tipo = ? ORDER BY data_geracao DESC";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, tipo);

            rs = stmt.executeQuery();

            List<Relatorio> result = new ArrayList<>();
            while (rs.next()) {
                result.add(mapRowToEntity(rs));
            }
            return result;

        } catch (SQLException e) {
            throw new ConnectionException("Erro ao buscar relatórios por tipo: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.closeResources(null, stmt, rs);
        }
    }

    @Override
    public List<Relatorio> findByCategoria(String categoria) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM relatorio WHERE categoria = ? ORDER BY data_geracao DESC";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, categoria);

            rs = stmt.executeQuery();

            List<Relatorio> result = new ArrayList<>();
            while (rs.next()) {
                result.add(mapRowToEntity(rs));
            }
            return result;

        } catch (SQLException e) {
            throw new ConnectionException("Erro ao buscar relatórios por categoria: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.closeResources(null, stmt, rs);
        }
    }

    @Override
    public List<Relatorio> findByUsuarioIdAndCategoria(Long usuarioId, String categoria) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM relatorio WHERE usuario_id = ? AND categoria = ? ORDER BY data_geracao DESC";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, usuarioId);
            stmt.setString(2, categoria);

            rs = stmt.executeQuery();

            List<Relatorio> result = new ArrayList<>();
            while (rs.next()) {
                result.add(mapRowToEntity(rs));
            }
            return result;

        } catch (SQLException e) {
            throw new ConnectionException("Erro ao buscar relatórios por usuário e categoria: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.closeResources(null, stmt, rs);
        }
    }

    @Override
    public List<Relatorio> findByDataGeracaoBetween(LocalDate inicio, LocalDate fim) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM relatorio WHERE data_geracao BETWEEN ? AND ? ORDER BY data_geracao DESC";
            stmt = conn.prepareStatement(sql);
            stmt.setDate(1, Date.valueOf(inicio));
            stmt.setDate(2, Date.valueOf(fim));

            rs = stmt.executeQuery();

            List<Relatorio> result = new ArrayList<>();
            while (rs.next()) {
                result.add(mapRowToEntity(rs));
            }
            return result;

        } catch (SQLException e) {
            throw new ConnectionException("Erro ao buscar relatórios por período: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.closeResources(null, stmt, rs);
        }
    }

    @Override
    public List<Relatorio> findByUsuario(Usuario usuario) {
        if (usuario == null || usuario.getId() == null) {
            throw new IllegalArgumentException("Usuário ou ID do usuário não pode ser nulo");
        }
        return findByUsuarioId(usuario.getId());
    }
}