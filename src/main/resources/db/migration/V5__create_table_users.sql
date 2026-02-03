/* Cria tabela de Usuários */

CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

-- Senha: '123' (hash BCrypt gerado para teste)
INSERT INTO usuarios (email, senha, role) VALUES 
('admin@seplag.mt.gov.br', '$2a$12$aytsJl0XJEsQrodJzBB4Du4rvlr5DeCt7cMMuMEdElsLUN2/BzDba', 'ADMIN');