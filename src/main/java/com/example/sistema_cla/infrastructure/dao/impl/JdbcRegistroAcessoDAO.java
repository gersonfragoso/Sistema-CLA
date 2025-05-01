package com.example.sistema_cla.infrastructure.dao.impl;

import com.example.sistema_cla.domain.model.EstatisticaAcesso;
import com.example.sistema_cla.domain.model.RegistroAcesso;
import com.example.sistema_cla.domain.model.Usuario;
import com.example.sistema_cla.infrastructure.banco.DatabaseConnection;
import com.example.sistema_cla.infrastructure.dao.interfaces.RegistroAcessoDAO;
import com.example.sistema_cla.infrastructure.exceptions.ConnectionException;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcRegistroAcessoDAO extends JdbcGenericDAO<RegistroAcesso, Long> implements RegistroAcessoDAO {

    @Override
    protected String getTableName() {
        return "registro_acesso";
    }

    @Override
    protected String getIdColumnName() {
        return "id";
    }

    @Override
    protected RegistroAcesso mapRowToEntity(ResultSet rs) throws SQLException {
        RegistroAcesso registro = new RegistroAcesso();
        registro.setId(rs.getLong("id"));

        // Converter para LocalDateTime
        Timestamp dataAcesso = rs.getTimestamp("data_acesso");
        if (dataAcesso != null) {
            registro.setDataAcesso(dataAcesso.toLocalDateTime());
        }

        registro.setTempoSessaoMinutos(rs.getInt("tempo_sessao_minutos"));
        registro.setPaginasVisitadas(rs.getInt("paginas_visitadas"));
        registro.setIpAcesso(rs.getString("ip_acesso"));
        registro.setDispositivo(rs.getString("dispositivo"));

        // Para relacionamentos, apenas armazenamos os IDs
        Long usuarioId = rs.getLong("usuario_id");
        if (!rs.wasNull()) {
            Usuario usuario = new Usuario();
            usuario.setId(usuarioId);
            registro.setUsuario(usuario);
        }

        return registro;
    }

    @Override
    protected PreparedStatement prepareInsertStatement(Connection conn, RegistroAcesso entity) throws SQLException {
        String sql = "INSERT INTO registro_acesso (usuario_id, data_acesso, tempo_sessao_minutos, " +
                "paginas_visitadas, ip_acesso, dispositivo) VALUES (?, ?, ?, ?, ?, ?) RETURNING id";

        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        if (entity.getUsuario() != null && entity.getUsuario().getId() != null) {
            stmt.setLong(1, entity.getUsuario().getId());
        } else {
            stmt.setNull(1, Types.BIGINT);
        }

        if (entity.getDataAcesso() != null) {
            stmt.setTimestamp(2, Timestamp.valueOf(entity.getDataAcesso()));
        } else {
            stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
        }

        stmt.setInt(3, entity.getTempoSessaoMinutos());
        stmt.setInt(4, entity.getPaginasVisitadas());
        stmt.setString(5, entity.getIpAcesso());
        stmt.setString(6, entity.getDispositivo());

        return stmt;
    }

    @Override
    protected PreparedStatement prepareUpdateStatement(Connection conn, RegistroAcesso entity) throws SQLException {
        String sql = "UPDATE registro_acesso SET usuario_id = ?, data_acesso = ?, " +
                "tempo_sessao_minutos = ?, paginas_visitadas = ?, ip_acesso = ?, " +
                "dispositivo = ? WHERE id = ?";

        PreparedStatement stmt = conn.prepareStatement(sql);

        if (entity.getUsuario() != null && entity.getUsuario().getId() != null) {
            stmt.setLong(1, entity.getUsuario().getId());
        } else {
            stmt.setNull(1, Types.BIGINT);
        }

        if (entity.getDataAcesso() != null) {
            stmt.setTimestamp(2, Timestamp.valueOf(entity.getDataAcesso()));
        } else {
            stmt.setNull(2, Types.TIMESTAMP);
        }

        stmt.setInt(3, entity.getTempoSessaoMinutos());
        stmt.setInt(4, entity.getPaginasVisitadas());
        stmt.setString(5, entity.getIpAcesso());
        stmt.setString(6, entity.getDispositivo());
        stmt.setLong(7, entity.getId());

        return stmt;
    }

    @Override
    protected boolean isNewEntity(RegistroAcesso entity) {
        return entity.getId() == null;
    }

    @Override
    protected RegistroAcesso getUpdatedEntityWithId(RegistroAcesso entity, ResultSet rs) throws SQLException {
        entity.setId(rs.getLong(1));
        return entity;
    }

    @Override
    protected Long getEntityId(RegistroAcesso entity) {
        return entity.getId();
    }

    @Override
    protected void setIdParameter(PreparedStatement stmt, Long id) throws SQLException {
        stmt.setLong(1, id);
    }

    @Override
    public List<RegistroAcesso> findByUsuarioId(Long usuarioId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM registro_acesso WHERE usuario_id = ? ORDER BY data_acesso DESC";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, usuarioId);

            rs = stmt.executeQuery();

            List<RegistroAcesso> result = new ArrayList<>();
            while (rs.next()) {
                result.add(mapRowToEntity(rs));
            }
            return result;

        } catch (SQLException e) {
            throw new ConnectionException("Erro ao buscar registros por usuário ID: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.closeResources(null, stmt, rs);
        }
    }

    @Override
    public List<RegistroAcesso> findByDataAcessoBetween(LocalDateTime inicio, LocalDateTime fim) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM registro_acesso WHERE data_acesso BETWEEN ? AND ? ORDER BY data_acesso DESC";
            stmt = conn.prepareStatement(sql);
            stmt.setTimestamp(1, Timestamp.valueOf(inicio));
            stmt.setTimestamp(2, Timestamp.valueOf(fim));

            rs = stmt.executeQuery();

            List<RegistroAcesso> result = new ArrayList<>();
            while (rs.next()) {
                result.add(mapRowToEntity(rs));
            }
            return result;

        } catch (SQLException e) {
            throw new ConnectionException("Erro ao buscar registros por período: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.closeResources(null, stmt, rs);
        }
    }

    @Override
    public List<RegistroAcesso> findByUsuarioIdAndDataAcessoBetween(
            Long usuarioId, LocalDateTime inicio, LocalDateTime fim) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM registro_acesso WHERE usuario_id = ? AND data_acesso BETWEEN ? AND ? " +
                    "ORDER BY data_acesso DESC";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, usuarioId);
            stmt.setTimestamp(2, Timestamp.valueOf(inicio));
            stmt.setTimestamp(3, Timestamp.valueOf(fim));

            rs = stmt.executeQuery();

            List<RegistroAcesso> result = new ArrayList<>();
            while (rs.next()) {
                result.add(mapRowToEntity(rs));
            }
            return result;

        } catch (SQLException e) {
            throw new ConnectionException("Erro ao buscar registros por usuário e período: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.closeResources(null, stmt, rs);
        }
    }

    @Override
    public List<EstatisticaAcesso> getEstatisticasAcessoPorPeriodo(LocalDate inicio, LocalDate fim) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime fimDateTime = fim.plusDays(1).atStartOfDay().minusNanos(1);

        try {
            conn = DatabaseConnection.getConnection();
            String sql =
                    "SELECT " +
                            "    u.id AS usuario_id, " +
                            "    CONCAT(u.nome, ' ', u.sobrenome) AS nome_usuario, " +
                            "    COUNT(ra.id) AS quantidade_acessos, " +
                            "    MAX(ra.data_acesso) AS ultimo_acesso, " +
                            "    SUM(ra.tempo_sessao_minutos) AS tempo_total_minutos, " +
                            "    SUM(ra.paginas_visitadas) AS paginas_visitadas " +
                            "FROM " +
                            "    registro_acesso ra " +
                            "    JOIN usuario u ON ra.usuario_id = u.id " +
                            "WHERE " +
                            "    ra.data_acesso BETWEEN ? AND ? " +
                            "GROUP BY " +
                            "    u.id, nome_usuario " +
                            "ORDER BY " +
                            "    quantidade_acessos DESC";

            stmt = conn.prepareStatement(sql);
            stmt.setTimestamp(1, Timestamp.valueOf(inicioDateTime));
            stmt.setTimestamp(2, Timestamp.valueOf(fimDateTime));

            rs = stmt.executeQuery();

            List<EstatisticaAcesso> result = new ArrayList<>();
            while (rs.next()) {
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

                result.add(estatistica);
            }
            return result;

        } catch (SQLException e) {
            throw new ConnectionException("Erro ao buscar estatísticas de acesso: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.closeResources(null, stmt, rs);
        }
    }

    @Override
    public EstatisticaAcesso getEstatisticaAcessoUsuario(Long usuarioId, LocalDate inicio, LocalDate fim) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime fimDateTime = fim.plusDays(1).atStartOfDay().minusNanos(1);

        try {
            conn = DatabaseConnection.getConnection();
            String sql =
                    "SELECT " +
                            "    u.id AS usuario_id, " +
                            "    CONCAT(u.nome, ' ', u.sobrenome) AS nome_usuario, " +
                            "    COUNT(ra.id) AS quantidade_acessos, " +
                            "    MAX(ra.data_acesso) AS ultimo_acesso, " +
                            "    SUM(ra.tempo_sessao_minutos) AS tempo_total_minutos, " +
                            "    SUM(ra.paginas_visitadas) AS paginas_visitadas " +
                            "FROM " +
                            "    registro_acesso ra " +
                            "    JOIN usuario u ON ra.usuario_id = u.id " +
                            "WHERE " +
                            "    ra.usuario_id = ? AND ra.data_acesso BETWEEN ? AND ? " +
                            "GROUP BY " +
                            "    u.id, nome_usuario";

            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, usuarioId);
            stmt.setTimestamp(2, Timestamp.valueOf(inicioDateTime));
            stmt.setTimestamp(3, Timestamp.valueOf(fimDateTime));

            rs = stmt.executeQuery();

            if (rs.next()) {
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

            return null;

        } catch (SQLException e) {
            throw new ConnectionException("Erro ao buscar estatísticas de acesso: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.closeResources(null, stmt, rs);
        }
    }

    @Override
    public Map<String, Object> getResumoGeralAcessos(LocalDate inicio, LocalDate fim) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime fimDateTime = fim.plusDays(1).atStartOfDay().minusNanos(1);

        try {
            conn = DatabaseConnection.getConnection();
            String sql =
                    "SELECT " +
                            "    COUNT(ra.id) AS total_acessos, " +
                            "    COUNT(DISTINCT ra.usuario_id) AS usuarios_ativos, " +
                            "    AVG(ra.tempo_sessao_minutos) AS tempo_medio_sessao " +
                            "FROM " +
                            "    registro_acesso ra " +
                            "WHERE " +
                            "    ra.data_acesso BETWEEN ? AND ?";

            stmt = conn.prepareStatement(sql);
            stmt.setTimestamp(1, Timestamp.valueOf(inicioDateTime));
            stmt.setTimestamp(2, Timestamp.valueOf(fimDateTime));

            rs = stmt.executeQuery();

            Map<String, Object> resumo = new HashMap<>();
            if (rs.next()) {
                resumo.put("totalAcessos", rs.getInt("total_acessos"));
                resumo.put("usuariosAtivos", rs.getInt("usuarios_ativos"));
                resumo.put("tempoMedioSessaoMinutos", Math.round(rs.getDouble("tempo_medio_sessao")));

                // Calcular média de acessos por usuário
                int totalAcessos = rs.getInt("total_acessos");
                int usuariosAtivos = rs.getInt("usuarios_ativos");
                double mediaAcessosPorUsuario = 0.0;

                if (usuariosAtivos > 0) {
                    mediaAcessosPorUsuario = (double) totalAcessos / usuariosAtivos;
                }

                resumo.put("mediaAcessosPorUsuario", mediaAcessosPorUsuario);
            }

            return resumo;

        } catch (SQLException e) {
            throw new ConnectionException("Erro ao buscar resumo geral de acessos: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.closeResources(null, stmt, rs);
        }
    }
}