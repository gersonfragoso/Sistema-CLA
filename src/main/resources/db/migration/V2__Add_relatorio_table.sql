CREATE TABLE IF NOT EXISTS relatorio (
    id SERIAL PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    data_geracao DATE NOT NULL,
    conteudo TEXT NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    categoria VARCHAR(50),
    usuario_id BIGINT,
    periodo_inicio DATE,
    periodo_fim DATE,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

-- Índices para otimizar consultas
CREATE INDEX IF NOT EXISTS idx_relatorio_usuario_id ON relatorio(usuario_id);
CREATE INDEX IF NOT EXISTS idx_relatorio_tipo ON relatorio(tipo);
CREATE INDEX IF NOT EXISTS idx_relatorio_categoria ON relatorio(categoria);
CREATE INDEX IF NOT EXISTS idx_relatorio_data_geracao ON relatorio(data_geracao);