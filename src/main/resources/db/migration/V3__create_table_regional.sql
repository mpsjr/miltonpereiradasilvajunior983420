-- Tabela Regional
-- Os dados serão inseridos via sincronização com API Externa (https://integrador-argus-api.geia.vip/v1/regionais)
CREATE TABLE IF NOT EXISTS regional (
    id INTEGER PRIMARY KEY, -- ID não é serial pois virá da API externa
    nome VARCHAR(200),
    ativo BOOLEAN DEFAULT TRUE
);