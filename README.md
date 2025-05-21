-- DROP das tabelas antigas se existirem para não dar erro na criação
DROP TABLE IF EXISTS usuario_ocupacao CASCADE;
DROP TABLE IF EXISTS ocupacao CASCADE;
DROP TABLE IF EXISTS user_permission CASCADE;
DROP TABLE IF EXISTS permission CASCADE;
DROP TABLE IF EXISTS lead_historico CASCADE;
DROP TABLE IF EXISTS documentos_lead CASCADE;
DROP TABLE IF EXISTS endereco_lead CASCADE;
DROP TABLE IF EXISTS leads CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- USERS
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nome VARCHAR(150),
    telefone VARCHAR(20),
    email VARCHAR(150),
    account_non_expired BOOLEAN DEFAULT TRUE,
    account_non_locked BOOLEAN DEFAULT TRUE,
    credentials_non_expired BOOLEAN DEFAULT TRUE,
    enabled BOOLEAN DEFAULT TRUE
);

-- PERMISSIONS
CREATE TABLE IF NOT EXISTS permission (
    id SERIAL PRIMARY KEY,
    description VARCHAR(255) NOT NULL UNIQUE
);

-- USER_PERMISSION (muitos-para-muitos)
CREATE TABLE IF NOT EXISTS user_permission (
    user_id INTEGER NOT NULL,
    permission_id INTEGER NOT NULL,
    PRIMARY KEY (user_id, permission_id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_permission FOREIGN KEY (permission_id) REFERENCES permission(id) ON DELETE CASCADE
);

-- Permissões padrão
INSERT INTO permission (description) VALUES
    ('ADMIN'),
    ('MANAGER'),
    ('COMMON_USER')
ON CONFLICT (description) DO NOTHING;

-- Usuários de exemplo
INSERT INTO users (user_name, password, nome, telefone, email)
VALUES
    ('admin', '$2a$10$WzKcyqF9XJW5Ib7KvT1hXOwCzRHvP6eEZrHxv.BXOWj3jBEEz7V2y', 'Administrador', '62999999999', 'admin@email.com'),
    ('user', '$2a$10$WzKcyqF9XJW5Ib7KvT1hXOwCzRHvP6eEZrHxv.BXOWj3jBEEz7V2y', 'Usuário Padrão', '62888888888', 'user@email.com')
ON CONFLICT (user_name) DO NOTHING;

-- USER_PERMISSION exemplo
INSERT INTO user_permission (user_id, permission_id)
VALUES
    (1, 1), -- admin -> ADMIN
    (2, 3); -- user -> COMMON_USER

-----------------------------------------------------------------------------------

-- OCUPACAO
CREATE TABLE IF NOT EXISTS ocupacao (
    id SERIAL PRIMARY KEY,
    descricao VARCHAR(100) NOT NULL UNIQUE
);

-- USUARIO_OCUPACAO
CREATE TABLE IF NOT EXISTS usuario_ocupacao (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    ocupacao_id INTEGER NOT NULL,
    CONSTRAINT fk_user_ocupacao FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_ocupacao_ocupacao FOREIGN KEY (ocupacao_id) REFERENCES ocupacao(id),
    CONSTRAINT unique_usuario_ocupacao UNIQUE (user_id, ocupacao_id)
);

-----------------------------------------------------------------------------------
-- LEADS
CREATE TABLE IF NOT EXISTS leads (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    cpf_cnpj VARCHAR(20),
    telefone VARCHAR(20),
    origem VARCHAR(100),
    status_leads VARCHAR(30),
    observacao TEXT,
    corretor_id INTEGER,
    CONSTRAINT fk_corretor FOREIGN KEY (corretor_id) REFERENCES users(id)
);

-- ENDERECO_LEAD
CREATE TABLE IF NOT EXISTS endereco_lead (
    id SERIAL PRIMARY KEY,
    lead_id INTEGER NOT NULL,
    logradouro VARCHAR(200),
    numero VARCHAR(20),
    complemento VARCHAR(100),
    bairro VARCHAR(100),
    cidade VARCHAR(100),
    estado VARCHAR(50),
    cep VARCHAR(20),
    CONSTRAINT fk_lead_endereco FOREIGN KEY (lead_id) REFERENCES leads(id) ON DELETE CASCADE
);

-- DOCUMENTOS DO LEAD
CREATE TABLE IF NOT EXISTS documentos_lead (
    id SERIAL PRIMARY KEY,
    nome_arquivo VARCHAR(255),
    tipo_arquivo VARCHAR(50),
    data_upload TIMESTAMP,
    conteudo BYTEA,
    lead_id INTEGER,
    CONSTRAINT fk_lead_documento FOREIGN KEY (lead_id) REFERENCES leads(id)
);

-- HISTÓRICO DO LEAD
CREATE TABLE IF NOT EXISTS lead_historico (
    id SERIAL PRIMARY KEY,
    lead_id INTEGER,
    data_modificacao TIMESTAMP,
    acao VARCHAR(100),
    user_id INTEGER,
    CONSTRAINT fk_lead_historico_lead FOREIGN KEY (lead_id) REFERENCES leads(id),
    CONSTRAINT fk_lead_historico_user FOREIGN KEY (user_id) REFERENCES users(id)
);
