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
----------------------------------------------------------------------------------------------------------xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
-- DROP das tabelas para evitar conflito na criação
DROP TABLE IF EXISTS lead_historico CASCADE;
DROP TABLE IF EXISTS documentos_lead CASCADE;
DROP TABLE IF EXISTS tipo_documento CASCADE;
DROP TABLE IF EXISTS endereco_lead CASCADE;
DROP TABLE IF EXISTS leads CASCADE;
DROP TABLE IF EXISTS usuario_ocupacao CASCADE;
DROP TABLE IF EXISTS ocupacao CASCADE;
DROP TABLE IF EXISTS user_permission CASCADE;
DROP TABLE IF EXISTS permission CASCADE;
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

-- LEADS (agora com codigo_upload)
CREATE TABLE IF NOT EXISTS leads (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    cpf_cnpj VARCHAR(20),
    telefone VARCHAR(20),
    origem VARCHAR(100),
    status_leads VARCHAR(30),
    observacao TEXT,
    corretor_id INTEGER,
    correspondente_id INTEGER,
    status_lead VARCHAR(50),
    codigo_upload UUID UNIQUE, -- NOVO campo para upload público
    CONSTRAINT fk_corretor FOREIGN KEY (corretor_id) REFERENCES users(id),
    CONSTRAINT fk_correspondente_lead FOREIGN KEY (correspondente_id) REFERENCES users(id)
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

-- TIPO_DOCUMENTO
CREATE TABLE IF NOT EXISTS tipo_documento (
    id SERIAL PRIMARY KEY,
    descricao VARCHAR(100) NOT NULL UNIQUE
);

-- DOCUMENTOS DO LEAD
CREATE TABLE IF NOT EXISTS documentos_lead (
    id SERIAL PRIMARY KEY,
    nome_arquivo VARCHAR(255),
    tipo_arquivo VARCHAR(50),
    data_upload DATE,
    conteudo BYTEA,
    lead_id INTEGER,
    tipo_documento_id INTEGER,
    data_emissao DATE,
    CONSTRAINT fk_lead_documento FOREIGN KEY (lead_id) REFERENCES leads(id),
    CONSTRAINT fk_tipo_documento FOREIGN KEY (tipo_documento_id) REFERENCES tipo_documento(id)
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
-------------------------------------------------------------------------------------xxxxxxxxxxx
População

-- Ative a extensão uuid para geração dos códigos únicos, se ainda não estiver ativa
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Permissões
INSERT INTO permission (description) VALUES
    ('ROLE_ADMIN'),
    ('ROLE_MANAGER'),
    ('ROLE_COMMON_USER'),
    ('ROLE_CORRETOR'),
    ('ROLE_CORRESPONDENTE')
ON CONFLICT (description) DO NOTHING;

-- Usuários
INSERT INTO users (user_name, password, nome, telefone, email)
VALUES
    ('admin',  '$2a$10$WzKcyqF9XJW5Ib7KvT1hXOwCzRHvP6eEZrHxv.BXOWj3jBEEz7V2y', 'Administrador',     '62999999999', 'admin@email.com'),
    ('user',   '$2a$10$WzKcyqF9XJW5Ib7KvT1hXOwCzRHvP6eEZrHxv.BXOWj3jBEEz7V2y', 'Usuário Padrão',    '62888888888', 'user@email.com'),
    ('joao',   '$2a$10$WzKcyqF9XJW5Ib7KvT1hXOwCzRHvP6eEZrHxv.BXOWj3jBEEz7V2y', 'João da Silva',     '62990000001', 'joao@email.com'),
    ('maria',  '$2a$10$WzKcyqF9XJW5Ib7KvT1hXOwCzRHvP6eEZrHxv.BXOWj3jBEEz7V2y', 'Maria Oliveira',    '62990000002', 'maria@email.com'),
    ('carlos', '$2a$10$WzKcyqF9XJW5Ib7KvT1hXOwCzRHvP6eEZrHxv.BXOWj3jBEEz7V2y', 'Carlos Souza',      '62990000003', 'carlos@email.com'),
    ('ana',    '$2a$10$WzKcyqF9XJW5Ib7KvT1hXOwCzRHvP6eEZrHxv.BXOWj3jBEEz7V2y', 'Ana Paula',         '62990000004', 'ana@email.com'),
    ('bruno',  '$2a$10$WzKcyqF9XJW5Ib7KvT1hXOwCzRHvP6eEZrHxv.BXOWj3jBEEz7V2y', 'Bruno Santos',      '62990000005', 'bruno@email.com')
ON CONFLICT (user_name) DO NOTHING;

-- Associação user_permission
INSERT INTO user_permission (user_id, permission_id)
SELECT u.id, p.id FROM users u, permission p
WHERE u.user_name = 'admin' AND p.description = 'ADMIN'
ON CONFLICT DO NOTHING;

INSERT INTO user_permission (user_id, permission_id)
SELECT u.id, p.id FROM users u, permission p
WHERE u.user_name = 'user' AND p.description = 'COMMON_USER'
ON CONFLICT DO NOTHING;

INSERT INTO user_permission (user_id, permission_id)
SELECT u.id, p.id FROM users u, permission p
WHERE u.user_name = 'joao' AND p.description = 'CORRETOR'
ON CONFLICT DO NOTHING;

INSERT INTO user_permission (user_id, permission_id)
SELECT u.id, p.id FROM users u, permission p
WHERE u.user_name = 'maria' AND p.description = 'CORRESPONDENTE'
ON CONFLICT DO NOTHING;

INSERT INTO user_permission (user_id, permission_id)
SELECT u.id, p.id FROM users u, permission p
WHERE u.user_name = 'carlos' AND p.description = 'COMMON_USER'
ON CONFLICT DO NOTHING;

INSERT INTO user_permission (user_id, permission_id)
SELECT u.id, p.id FROM users u, permission p
WHERE u.user_name = 'ana' AND p.description = 'COMMON_USER'
ON CONFLICT DO NOTHING;

INSERT INTO user_permission (user_id, permission_id)
SELECT u.id, p.id FROM users u, permission p
WHERE u.user_name = 'bruno' AND p.description = 'COMMON_USER'
ON CONFLICT DO NOTHING;

-- Ocupações
INSERT INTO ocupacao (descricao) VALUES
    ('Gestor de Leads'),
    ('Supervisor'),
    ('Corretor Júnior'),
    ('Correspondente Bancário')
ON CONFLICT (descricao) DO NOTHING;

-- Associação usuario_ocupacao
INSERT INTO usuario_ocupacao (user_id, ocupacao_id)
SELECT u.id, o.id FROM users u, ocupacao o
WHERE u.user_name = 'joao' AND o.descricao = 'Corretor Júnior'
ON CONFLICT DO NOTHING;

INSERT INTO usuario_ocupacao (user_id, ocupacao_id)
SELECT u.id, o.id FROM users u, ocupacao o
WHERE u.user_name = 'maria' AND o.descricao = 'Correspondente Bancário'
ON CONFLICT DO NOTHING;

INSERT INTO usuario_ocupacao (user_id, ocupacao_id)
SELECT u.id, o.id FROM users u, ocupacao o
WHERE u.user_name = 'admin' AND o.descricao = 'Gestor de Leads'
ON CONFLICT DO NOTHING;

INSERT INTO usuario_ocupacao (user_id, ocupacao_id)
SELECT u.id, o.id FROM users u, ocupacao o
WHERE u.user_name = 'user' AND o.descricao = 'Supervisor'
ON CONFLICT DO NOTHING;

-- Tipos de Documento
INSERT INTO tipo_documento (descricao) VALUES
    ('COMPROVANTE_ENDERECO'),
    ('RG'),
    ('CPF'),
    ('CONTRACHEQUE')
ON CONFLICT (descricao) DO NOTHING;

-- Leads (com código_upload gerado)
INSERT INTO leads (
    nome, cpf_cnpj, telefone, origem, status_leads, observacao,
    corretor_id, correspondente_id, status_lead, codigo_upload
) VALUES
(
    'João da Silva', '12345678901', '62990000001', 'Instagram', 'NOVO',
    'Primeiro contato realizado.',
    (SELECT id FROM users WHERE user_name = 'joao'),
    (SELECT id FROM users WHERE user_name = 'maria'),
    'NOVO',
    uuid_generate_v4()
),
(
    'Maria Oliveira', '98765432100', '62990000002', 'Indicação', 'EM_ATENDIMENTO',
    'Aguardando documentos.',
    (SELECT id FROM users WHERE user_name = 'joao'),
    (SELECT id FROM users WHERE user_name = 'maria'),
    'EM_ATENDIMENTO',
    uuid_generate_v4()
),
(
    'Carlos Souza', '11223344556', '62990000003', 'Facebook', 'NOVO',
    'Lead inserido pelo sistema.',
    (SELECT id FROM users WHERE user_name = 'joao'),
    null,
    'NOVO',
    uuid_generate_v4()
),
(
    'Ana Paula', '22334455667', '62990000004', 'Site', 'CONCLUIDO',
    'Lead convertido.',
    (SELECT id FROM users WHERE user_name = 'joao'),
    null,
    'CONCLUIDO',
    uuid_generate_v4()
),
(
    'Bruno Santos', '33445566778', '62990000005', 'WhatsApp', 'CANCELADO',
    'Desistiu do processo.',
    null,
    (SELECT id FROM users WHERE user_name = 'maria'),
    'CANCELADO',
    uuid_generate_v4()
)
ON CONFLICT DO NOTHING;

-- Endereços dos Leads
INSERT INTO endereco_lead (
    lead_id, logradouro, numero, complemento, bairro, cidade, estado, cep
) VALUES
(
    (SELECT id FROM leads WHERE nome = 'João da Silva'),
    'Rua Alfa', '123', 'Apto 1', 'Centro', 'Goiânia', 'GO', '74000-000'
),
(
    (SELECT id FROM leads WHERE nome = 'Maria Oliveira'),
    'Av. Beta', '456', null, 'Setor Sul', 'Goiânia', 'GO', '74001-000'
),
(
    (SELECT id FROM leads WHERE nome = 'Carlos Souza'),
    'Rua Gamma', '789', 'Casa', 'Vila Nova', 'Goiânia', 'GO', '74002-000'
),
(
    (SELECT id FROM leads WHERE nome = 'Ana Paula'),
    'Alameda Delta', '321', null, 'Centro', 'Anápolis', 'GO', '75000-000'
),
(
    (SELECT id FROM leads WHERE nome = 'Bruno Santos'),
    'Rua Épsilon', '654', null, 'Jardim', 'Aparecida', 'GO', '74900-000'
)
ON CONFLICT DO NOTHING;

-- Documentos dos Leads (conteudo = null)
INSERT INTO documentos_lead (
    nome_arquivo, tipo_arquivo, data_upload, conteudo, lead_id, tipo_documento_id, data_emissao
) VALUES
(
    'conta_joao.pdf', 'PDF', current_date, null,
    (SELECT id FROM leads WHERE nome = 'João da Silva'),
    (SELECT id FROM tipo_documento WHERE descricao = 'COMPROVANTE_ENDERECO'),
    '2024-05-01'
),
(
    'rg_maria.jpg', 'JPG', current_date, null,
    (SELECT id FROM leads WHERE nome = 'Maria Oliveira'),
    (SELECT id FROM tipo_documento WHERE descricao = 'RG'),
    '2023-02-14'
),
(
    'cpf_carlos.pdf', 'PDF', current_date, null,
    (SELECT id FROM leads WHERE nome = 'Carlos Souza'),
    (SELECT id FROM tipo_documento WHERE descricao = 'CPF'),
    '2022-08-20'
),
(
    'contracheque_ana.pdf', 'PDF', current_date, null,
    (SELECT id FROM leads WHERE nome = 'Ana Paula'),
    (SELECT id FROM tipo_documento WHERE descricao = 'CONTRACHEQUE'),
    '2024-04-15'
),
(
    'conta_bruno.pdf', 'PDF', current_date, null,
    (SELECT id FROM leads WHERE nome = 'Bruno Santos'),
    (SELECT id FROM tipo_documento WHERE descricao = 'COMPROVANTE_ENDERECO'),
    '2023-10-10'
)
ON CONFLICT DO NOTHING;

-- Histórico dos Leads
INSERT INTO lead_historico (lead_id, data_modificacao, acao, user_id)
SELECT l.id, now() - interval '15 day', 'Lead criado', u.id
FROM leads l, users u
WHERE l.nome = 'João da Silva' AND u.user_name = 'joao'
ON CONFLICT DO NOTHING;

INSERT INTO lead_historico (lead_id, data_modificacao, acao, user_id)
SELECT l.id, now() - interval '12 day', 'Contato realizado', u.id
FROM leads l, users u
WHERE l.nome = 'João da Silva' AND u.user_name = 'joao'
ON CONFLICT DO NOTHING;

INSERT INTO lead_historico (lead_id, data_modificacao, acao, user_id)
SELECT l.id, now() - interval '10 day', 'Lead criado', u.id
FROM leads l, users u
WHERE l.nome = 'Maria Oliveira' AND u.user_name = 'maria'
ON CONFLICT DO NOTHING;

INSERT INTO lead_historico (lead_id, data_modificacao, acao, user_id)
SELECT l.id, now() - interval '8 day', 'Documentos pendentes', u.id
FROM leads l, users u
WHERE l.nome = 'Maria Oliveira' AND u.user_name = 'maria'
ON CONFLICT DO NOTHING;

INSERT INTO lead_historico (lead_id, data_modificacao, acao, user_id)
SELECT l.id, now() - interval '6 day', 'Lead criado', u.id
FROM leads l, users u
WHERE l.nome = 'Carlos Souza' AND u.user_name = 'carlos'
ON CONFLICT DO NOTHING;

INSERT INTO lead_historico (lead_id, data_modificacao, acao, user_id)
SELECT l.id, now() - interval '4 day', 'Lead criado', u.id
FROM leads l, users u
WHERE l.nome = 'Ana Paula' AND u.user_name = 'ana'
ON CONFLICT DO NOTHING;

INSERT INTO lead_historico (lead_id, data_modificacao, acao, user_id)
SELECT l.id, now() - interval '2 day', 'Lead criado', u.id
FROM leads l, users u
WHERE l.nome = 'Bruno Santos' AND u.user_name = 'bruno'
ON CONFLICT DO NOTHING;

------------------------------------xxxxxxxxxxxxxxxxxxxxxxxxx

-- 1. Descubra os IDs necessários (só rode os SELECTs se quiser confirmar; não precisa copiar para o script principal)

-- SELECT id FROM users WHERE user_name = 'joao';                -- Exemplo: 3
-- SELECT id FROM tipo_documento WHERE descricao = 'COMPROVANTE_ENDERECO'; -- Exemplo: 1

-- 2. Insere o lead sem correspondente
INSERT INTO leads (
nome, cpf_cnpj, telefone, origem, status_leads, observacao,
corretor_id, correspondente_id, status_lead, codigo_upload
)
VALUES (
'Lead Apto Correspondente',
'99887766554',
'62999990010',
'Google',
'NOVO',
'Teste: lead apto para correspondente',
(SELECT id FROM users WHERE user_name = 'joao'),
NULL,
'NOVO',
uuid_generate_v4()
)
RETURNING id;

-- 3. Depois de rodar o insert acima, ANOTE o id retornado (exemplo: 12) e insira o documento
-- Troque 12 pelo id retornado e ajuste o tipo_documento_id se necessário (veja no SELECT acima)

INSERT INTO documentos_lead (
nome_arquivo, tipo_arquivo, data_upload, conteudo, lead_id, tipo_documento_id, data_emissao
) VALUES (
'comprovante_endereco_lead_apto.pdf', 'PDF', current_date, null,
(SELECT id FROM leads WHERE nome = 'Lead Apto Correspondente' ORDER BY id DESC LIMIT 1),
(SELECT id FROM tipo_documento WHERE descricao = 'COMPROVANTE_ENDERECO'),
(current_date - interval '1 month')
);




