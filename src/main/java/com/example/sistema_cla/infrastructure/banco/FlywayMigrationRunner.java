package com.example.sistema_cla.infrastructure.banco;

import com.example.sistema_cla.infrastructure.exceptions.ConnectionException;
import org.flywaydb.core.Flyway;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

@Component
public class FlywayMigrationRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            Flyway flyway = Flyway.configure()
                    .dataSource(DatabaseConnection.URL, DatabaseConnection.USER, DatabaseConnection.PASSWORD)
                    .locations("classpath:db/migration")
                    .baselineOnMigrate(true)
                    .load();

            flyway.migrate();
        } catch (SQLException e) {
            throw new ConnectionException("Erro ao executar migrações Flyway", e);
        }
    }
}