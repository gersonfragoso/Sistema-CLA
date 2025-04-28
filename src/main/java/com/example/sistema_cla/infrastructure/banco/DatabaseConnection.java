package com.example.sistema_cla.infrastructure.banco;

import com.example.sistema_cla.infrastructure.exceptions.ConnectionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/sistema_cla";
    private static final String USER = "postgres";
    private static final String PASSWORD = "gerson1550";

    private static Connection connection;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new ConnectionException("Driver PostgreSQL não encontrado", e);
        }
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
            return connection;
        } catch (SQLException e) {
            throw new ConnectionException("Erro ao conectar ao banco de dados", e);
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new ConnectionException("Erro ao fechar conexão com o banco de dados", e);
        }
    }

    // Método utilitário para fechar recursos
    public static void closeResources(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            throw new ConnectionException("Erro ao fechar recursos do banco de dados", e);
        }
    }
}