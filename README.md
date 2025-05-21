-- USERS (antigo usuario)
CREATE TABLE IF NOT EXISTS user (
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

-- PERMISSIONS (antigo ocupacao)
CREATE TABLE IF NOT EXISTS permission (
    id SERIAL PRIMARY KEY,
    description VARCHAR(255) NOT NULL UNIQUE
);

-- USER_PERMISSION (antigo usuario_ocupacao)
CREATE TABLE IF NOT EXISTS user_permission (
    user_id INTEGER NOT NULL,
    permission_id INTEGER NOT NULL,
    PRIMARY KEY (user_id, permission_id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    CONSTRAINT fk_permission FOREIGN KEY (permission_id) REFERENCES permission(id) ON DELETE CASCADE
);

-- Inserção de permissões padrão
INSERT INTO permission (description) VALUES 
    ('ADMIN'),
    ('MANAGER'),
    ('COMMON_USER')
ON CONFLICT DO NOTHING;

-- Exemplo de inserção de usuários (senha BCRYPT exemplo)
INSERT INTO user (user_name, password, nome, telefone, email)
VALUES 
    ('admin', '$2a$10$WzKcyqF9XJW5Ib7KvT1hXOwCzRHvP6eEZrHxv.BXOWj3jBEEz7V2y', 'Administrador', '62999999999', 'admin@email.com'),
    ('user', '$2a$10$WzKcyqF9XJW5Ib7KvT1hXOwCzRHvP6eEZrHxv.BXOWj3jBEEz7V2y', 'Usuário Padrão', '62888888888', 'user@email.com');

-- Relacionar permissões aos usuários (ajuste IDs conforme SELECT)
INSERT INTO user_permission (user_id, permission_id)
VALUES
    (1, 1), -- admin -> ADMIN
    (2, 3); -- user -> COMMON_USER

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
    corretor_id INTEGER, -- FK para user (corretor)
    CONSTRAINT fk_corretor FOREIGN KEY (corretor_id) REFERENCES user(id)
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
    conteudo LONGBLOB,
    lead_id INTEGER,
    CONSTRAINT fk_lead_documento FOREIGN KEY (lead_id) REFERENCES leads(id)
);

-- HISTÓRICO DO LEAD
CREATE TABLE IF NOT EXISTS lead_historico (
    id SERIAL PRIMARY KEY,
    lead_id INTEGER,
    data_modificacao TIMESTAMP,
    acao VARCHAR(100),
    user_id INTEGER, -- FK para user
    CONSTRAINT fk_lead_historico_lead FOREIGN KEY (lead_id) REFERENCES leads(id),
    CONSTRAINT fk_lead_historico_user FOREIGN KEY (user_id) REFERENCES user(id)
);
