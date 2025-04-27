package com.example.sistema_cla.infrastructure.dao.impl;

import com.example.sistema_cla.domain.model.Usuario;
import com.example.sistema_cla.infrastructure.banco.DatabaseConnection;
import com.example.sistema_cla.infrastructure.dao.interfaces.UsuarioDAO;
import com.example.sistema_cla.infrastructure.exceptions.ConnectionException;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Optional;

@Repository
public class JdbcUsuarioDAO extends JdbcGenericDAO<Usuario, Long> implements UsuarioDAO {

    @Override
    protected String getTableName() {
        return "usuario";
    }

    @Override
    protected String getIdColumnName() {
        return "id";
    }

    @Override
    protected Usuario mapRowToEntity(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getLong("id"));
        usuario.setNome(rs.getString("nome"));
        usuario.setSobrenome(rs.getString("sobrenome"));
        usuario.setCpf(rs.getString("cpf"));
        usuario.setEmail(rs.getString("email"));
        usuario.setSenha(rs.getString("senha"));

        // Converter para LocalDate
        Date dataNasc = rs.getDate("data_nascimento");
        if (dataNasc != null) {
            usuario.setDataNascimento(dataNasc.toLocalDate());
        }

        // Converter String para enum
        String tipoUsuarioStr = rs.getString("tipo_usuario");
        if (tipoUsuarioStr != null) {
            usuario.setTipoUsuario(com.example.sistema_cla.domain.enums.TipoUsuario.valueOf(tipoUsuarioStr));
        }

        usuario.setBloqueado(rs.getBoolean("bloqueado"));

        // Nota: Relacionamentos como endereco, telefone e avaliacoes geralmente
        // são carregados por consultas separadas ou lazy loading

        return usuario;
    }

    @Override
    protected PreparedStatement prepareInsertStatement(Connection conn, Usuario entity) throws SQLException {
        String sql = "INSERT INTO usuario (nome, sobrenome, cpf, email, senha, data_nascimento, " +
                "tipo_usuario, endereco_id, telefone_id, bloqueado) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, entity.getNome());
        stmt.setString(2, entity.getSobrenome());
        stmt.setString(3, entity.getCpf());
        stmt.setString(4, entity.getEmail());
        stmt.setString(5, entity.getSenha());

        if (entity.getDataNascimento() != null) {
            stmt.setDate(6, Date.valueOf(entity.getDataNascimento()));
        } else {
            stmt.setNull(6, Types.DATE);
        }

        if (entity.getTipoUsuario() != null) {
            stmt.setString(7, entity.getTipoUsuario().name());
        } else {
            stmt.setNull(7, Types.VARCHAR);
        }

        if (entity.getEndereco() != null && entity.getEndereco().getId() != null) {
            stmt.setLong(8, entity.getEndereco().getId());
        } else {
            stmt.setNull(8, Types.BIGINT);
        }

        if (entity.getTelefone() != null && entity.getTelefone().getId() != null) {
            stmt.setLong(9, entity.getTelefone().getId());
        } else {
            stmt.setNull(9, Types.BIGINT);
        }

        stmt.setBoolean(10, entity.isBloqueado());

        return stmt;
    }

    @Override
    protected PreparedStatement prepareUpdateStatement(Connection conn, Usuario entity) throws SQLException {
        String sql = "UPDATE usuario SET nome = ?, sobrenome = ?, cpf = ?, email = ?, senha = ?, " +
                "data_nascimento = ?, tipo_usuario = ?, endereco_id = ?, telefone_id = ?, " +
                "bloqueado = ? WHERE id = ?";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, entity.getNome());
        stmt.setString(2, entity.getSobrenome());
        stmt.setString(3, entity.getCpf());
        stmt.setString(4, entity.getEmail());
        stmt.setString(5, entity.getSenha());

        if (entity.getDataNascimento() != null) {
            stmt.setDate(6, Date.valueOf(entity.getDataNascimento()));
        } else {
            stmt.setNull(6, Types.DATE);
        }

        if (entity.getTipoUsuario() != null) {
            stmt.setString(7, entity.getTipoUsuario().name());
        } else {
            stmt.setNull(7, Types.VARCHAR);
        }

        if (entity.getEndereco() != null && entity.getEndereco().getId() != null) {
            stmt.setLong(8, entity.getEndereco().getId());
        } else {
            stmt.setNull(8, Types.BIGINT);
        }

        if (entity.getTelefone() != null && entity.getTelefone().getId() != null) {
            stmt.setLong(9, entity.getTelefone().getId());
        } else {
            stmt.setNull(9, Types.BIGINT);
        }

        stmt.setBoolean(10, entity.isBloqueado());
        stmt.setLong(11, entity.getId());

        return stmt;
    }

    @Override
    protected boolean isNewEntity(Usuario entity) {
        return entity.getId() == null;
    }

    @Override
    protected Usuario getUpdatedEntityWithId(Usuario entity, ResultSet rs) throws SQLException {
        entity.setId(rs.getLong(1));
        return entity;
    }

    @Override
    protected Long getEntityId(Usuario entity) {
        return entity.getId();
    }

    @Override
    protected void setIdParameter(PreparedStatement stmt, Long id) throws SQLException {
        stmt.setLong(1, id);
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM usuario WHERE email = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);

            rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapRowToEntity(rs));
            }
            return Optional.empty();

        } catch (SQLException e) {
            throw new ConnectionException("Erro ao buscar usuário por email: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.closeResources(null, stmt, rs);
        }
    }

    @Override
    public Optional<Usuario> findByCpf(String cpf) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM usuario WHERE cpf = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, cpf);

            rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapRowToEntity(rs));
            }
            return Optional.empty();

        } catch (SQLException e) {
            throw new ConnectionException("Erro ao buscar usuário por CPF: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.closeResources(null, stmt, rs);
        }
    }
}