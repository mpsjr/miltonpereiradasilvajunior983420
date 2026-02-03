CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

-- Senha: '123' (hash BCrypt gerado para teste)
INSERT INTO usuarios (email, senha, role) VALUES 
('admin@seplag.mt.gov.br', '$2y$10$wN1Q/R.M6G.Jb.k1B9Xb5eG1o5.7Q9XJ8RzL2Z4m3Z1l2K4n5o6p7', 'ADMIN');