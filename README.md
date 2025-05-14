-- Criação da tabela de permissões
CREATE TABLE IF NOT EXISTS permission (
    id SERIAL PRIMARY KEY,
    description VARCHAR(255) NOT NULL UNIQUE
);

-- Criação da tabela de usuários
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    account_non_expired BOOLEAN DEFAULT TRUE,
    account_non_locked BOOLEAN DEFAULT TRUE,
    credentials_non_expired BOOLEAN DEFAULT TRUE,
    enabled BOOLEAN DEFAULT TRUE
);

-- Tabela de relacionamento entre usuários e permissões
CREATE TABLE IF NOT EXISTS user_permission (
    id_user INTEGER NOT NULL,
    id_permission INTEGER NOT NULL,
    PRIMARY KEY (id_user, id_permission),
    CONSTRAINT fk_user FOREIGN KEY (id_user) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_permission FOREIGN KEY (id_permission) REFERENCES permission(id) ON DELETE CASCADE
);

-- Inserção de permissões padrão
INSERT INTO permission (description) VALUES 
    ('ADMIN'),
    ('MANAGER'),
    ('COMMON_USER')
ON CONFLICT DO NOTHING;

-- Gerar uma senha BCRYPT (substitua na linha abaixo)
-- Exemplo: senha = admin123 → hash gerado com BCrypt
-- Você pode gerar em Java com: new BCryptPasswordEncoder().encode("admin123")
-- Ou usar esse hash de exemplo: $2a$10$WzKcyqF9XJW5Ib7KvT1hXOwCzRHvP6eEZrHxv.BXOWj3jBEEz7V2y

-- Inserção de usuários
INSERT INTO users (user_name, password, account_non_expired, account_non_locked, credentials_non_expired, enabled)
VALUES 
    ('admin', '$2a$10$WzKcyqF9XJW5Ib7KvT1hXOwCzRHvP6eEZrHxv.BXOWj3jBEEz7V2y', true, true, true, true),
    ('user', '$2a$10$WzKcyqF9XJW5Ib7KvT1hXOwCzRHvP6eEZrHxv.BXOWj3jBEEz7V2y', true, true, true, true);

-- Relacionar permissões aos usuários
-- Verifica os IDs antes de inserir (use SELECT * FROM users; e SELECT * FROM permission;)
-- Exemplo com admin sendo ADMIN e user sendo COMMON_USER
INSERT INTO user_permission (id_user, id_permission)
VALUES
    (1, 1), -- admin -> ADMIN
    (2, 3); -- user -> COMMON_USER
