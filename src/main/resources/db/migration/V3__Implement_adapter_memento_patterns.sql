-- Alterações para o padrão Adapter (ViaCEP)
ALTER TABLE endereco
  RENAME COLUMN rua TO logradouro;

ALTER TABLE endereco
  ADD COLUMN IF NOT EXISTS bairro VARCHAR(255),
  ADD COLUMN IF NOT EXISTS complemento VARCHAR(255),
  ADD COLUMN IF NOT EXISTS pais VARCHAR(100) DEFAULT 'Brasil',
  ADD COLUMN IF NOT EXISTS validado BOOLEAN DEFAULT FALSE;

-- Alterar o tipo do número da casa para suportar formatos diversos
ALTER TABLE endereco
  RENAME COLUMN numero_casa TO numero;

ALTER TABLE endereco
  ALTER COLUMN numero TYPE VARCHAR(20);

-- Criar tabela para o padrão Memento (Histórico de avaliações)
CREATE TABLE IF NOT EXISTS avaliacao_historico (
    id SERIAL PRIMARY KEY,
    avaliacao_id BIGINT NOT NULL,
    nota INTEGER NOT NULL CHECK (nota BETWEEN 1 AND 5),
    comentario TEXT,
    data_registro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    usuario_id BIGINT NOT NULL,
    local_id BIGINT NOT NULL,
    data_avaliacao DATE,
    FOREIGN KEY (avaliacao_id) REFERENCES avaliacao(id) ON DELETE CASCADE,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id),
    FOREIGN KEY (local_id) REFERENCES local(id)
);

-- Índices para o histórico de avaliações
CREATE INDEX IF NOT EXISTS idx_avaliacao_historico_avaliacao_id ON avaliacao_historico(avaliacao_id);
CREATE INDEX IF NOT EXISTS idx_avaliacao_historico_usuario_id ON avaliacao_historico(usuario_id);
CREATE INDEX IF NOT EXISTS idx_avaliacao_historico_local_id ON avaliacao_historico(local_id);
CREATE INDEX IF NOT EXISTS idx_avaliacao_historico_data_registro ON avaliacao_historico(data_registro);