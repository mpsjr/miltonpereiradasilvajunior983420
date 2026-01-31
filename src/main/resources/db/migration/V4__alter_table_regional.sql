ALTER TABLE regional DROP CONSTRAINT regional_pkey; -- Remove a constraint de chave primária atual
ALTER TABLE regional RENAME COLUMN id TO id_regional; -- Renomeia a coluna 'id' para 'id_regional' (para ficar claro que é o ID externo que vem da API)
ALTER TABLE regional ADD COLUMN id BIGSERIAL PRIMARY KEY; -- Cria uma nova coluna para ser a chave primária da tabela
CREATE INDEX idx_regional_external_id ON regional(id_regional); -- Cria o índice para o ID externo
