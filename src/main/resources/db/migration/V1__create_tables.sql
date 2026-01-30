-- Tabela de Artistas
CREATE TABLE IF NOT EXISTS artista (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL
);

-- Tabela de Álbuns
CREATE TABLE IF NOT EXISTS album (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    ano_lancamento INTEGER
);

-- Tabela de Junção (N:N) - Artistas e Álbuns
CREATE TABLE IF NOT EXISTS artista_album (
    artista_id BIGINT NOT NULL,
    album_id BIGINT NOT NULL,
    PRIMARY KEY (artista_id, album_id),
    CONSTRAINT fk_artista FOREIGN KEY (artista_id) REFERENCES artista(id),
    CONSTRAINT fk_album FOREIGN KEY (album_id) REFERENCES album(id)
);

-- Tabela para múltiplas imagens/capas
CREATE TABLE imagem_album (
    id BIGSERIAL PRIMARY KEY,
    url VARCHAR(500) NOT NULL,
    album_id BIGINT NOT NULL,
    CONSTRAINT fk_imagem_album FOREIGN KEY (album_id) REFERENCES album(id)
);

-- Os dados serão inseridos em V2__carrega_artistas_albuns.sql