CREATE TABLE IF NOT EXISTS endereco (
    id SERIAL PRIMARY KEY,
    rua VARCHAR(255),
    cidade VARCHAR(255),
    estado VARCHAR(255),
    cep VARCHAR(20),
    numero_casa INTEGER
);

CREATE TABLE IF NOT EXISTS telefone (
    id SERIAL PRIMARY KEY,
    ddd INTEGER,
    numero_telefone VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS usuario (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    sobrenome VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    data_nascimento DATE,
    tipo_usuario VARCHAR(50),
    endereco_id BIGINT,
    telefone_id BIGINT,
    bloqueado BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (endereco_id) REFERENCES endereco(id),
    FOREIGN KEY (telefone_id) REFERENCES telefone(id)
);

CREATE TABLE IF NOT EXISTS local (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    endereco VARCHAR(255) NOT NULL,
    tipo_local VARCHAR(100) NOT NULL,
    status_acessibilidade VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS avaliacao (
    id SERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    local_id BIGINT NOT NULL,
    nota INTEGER NOT NULL CHECK (nota BETWEEN 1 AND 5),
    comentario TEXT,
    data_avaliacao DATE,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id),
    FOREIGN KEY (local_id) REFERENCES local(id)
);