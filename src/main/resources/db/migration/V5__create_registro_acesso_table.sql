-- V5__create_registro_acesso_table.sql
CREATE TABLE registro_acesso (
    id SERIAL PRIMARY KEY,
    usuario_id BIGINT,
    data_acesso TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tempo_sessao_minutos INTEGER NOT NULL DEFAULT 0,
    paginas_visitadas INTEGER NOT NULL DEFAULT 0,
    ip_acesso VARCHAR(50),
    dispositivo VARCHAR(255),
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

-- Índices para otimizar consultas
CREATE INDEX idx_registro_acesso_usuario_id ON registro_acesso(usuario_id);
CREATE INDEX idx_registro_acesso_data_acesso ON registro_acesso(data_acesso);