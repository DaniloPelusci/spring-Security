-- LEADS
CREATE TABLE leads (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(150) NOT NULL,
    cpf_cnpj VARCHAR(20),
    telefone VARCHAR(20),
    origem VARCHAR(100),
    status_leads VARCHAR(30),
    observacao TEXT,
    corretor_id BIGINT
);

-- ENDERECO_LEAD
CREATE TABLE endereco_lead (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    lead_id BIGINT NOT NULL,
    logradouro VARCHAR(200),
    numero VARCHAR(20),
    complemento VARCHAR(100),
    bairro VARCHAR(100),
    cidade VARCHAR(100),
    estado VARCHAR(50),
    cep VARCHAR(20),
    FOREIGN KEY (lead_id) REFERENCES leads(id) ON DELETE CASCADE
);

-- DOCUMENTOS DO LEAD
CREATE TABLE documentos_lead (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome_arquivo VARCHAR(255),
    tipo_arquivo VARCHAR(50),
    data_upload TIMESTAMP,
    conteudo LONGBLOB,
    lead_id BIGINT,
    FOREIGN KEY (lead_id) REFERENCES leads(id)
);

-- HISTÓRICO DO LEAD
CREATE TABLE lead_historico (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    lead_id BIGINT,
    data_modificacao TIMESTAMP,
    acao VARCHAR(100),
    usuario_id VARCHAR(100),
    FOREIGN KEY (lead_id) REFERENCES leads(id)
);

-- USUÁRIOS
CREATE TABLE usuario (
    id VARCHAR(100) PRIMARY KEY,  -- ID do Keycloak
    nome VARCHAR(150),
    telefone VARCHAR(20),
    email VARCHAR(150)
);

-- OCUPAÇÕES
CREATE TABLE ocupacao (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    descricao VARCHAR(100)
);

-- ASSOCIAÇÃO USUÁRIO-OCUPAÇÃO
CREATE TABLE usuario_ocupacao (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    usuario_id VARCHAR(255),
    ocupacao_id BIGINT,
    -- outros campos aqui...
    CONSTRAINT fk_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id),
    CONSTRAINT fk_ocupacao FOREIGN KEY (ocupacao_id) REFERENCES ocupacao(id),
    CONSTRAINT unique_usuario_ocupacao UNIQUE (usuario_id, ocupacao_id)
);

