-- Criação da tabela para armazenar estatísticas de acesso por usuário
CREATE TABLE estatistica_acesso (
    usuario_id BIGINT PRIMARY KEY,
    nome_usuario VARCHAR(255) NOT NULL,
    quantidade_acessos INTEGER NOT NULL DEFAULT 0,
    ultimo_acesso TIMESTAMP,
    tempo_total_minutos BIGINT NOT NULL DEFAULT 0,
    paginas_visitadas INTEGER NOT NULL DEFAULT 0,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

-- Índice para otimizar consultas por quantidade de acessos
CREATE INDEX idx_estatistica_quantidade_acessos ON estatistica_acesso(quantidade_acessos DESC);