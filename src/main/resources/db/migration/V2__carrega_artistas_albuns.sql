-- Carga Inicial de Artistas (Edital Anexo II-A)
INSERT INTO artista (nome) VALUES ('Serj Tankian'); -- ID 1
INSERT INTO artista (nome) VALUES ('Mike Shinoda'); -- ID 2
INSERT INTO artista (nome) VALUES ('Michel Teló'); -- ID 3
INSERT INTO artista (nome) VALUES ('Guns N'' Roses'); -- ID 4

-- Carga Inicial de Álbuns
-- Serj Tankian
INSERT INTO album (titulo, ano_lancamento) VALUES ('Harakiri', 2012); -- ID 1
INSERT INTO album (titulo, ano_lancamento) VALUES ('Black Blooms', 2019); -- ID 2
INSERT INTO album (titulo, ano_lancamento) VALUES ('The Rough Dog', 2021); -- ID 3

-- Mike Shinoda
INSERT INTO album (titulo, ano_lancamento) VALUES ('The Rising Tied', 2005); -- ID 4
INSERT INTO album (titulo, ano_lancamento) VALUES ('Post Traumatic', 2018); -- ID 5
INSERT INTO album (titulo, ano_lancamento) VALUES ('Post Traumatic EP', 2018); -- ID 6
INSERT INTO album (titulo, ano_lancamento) VALUES ('Where''d You Go', 2006); -- ID 7

-- Michel Teló
INSERT INTO album (titulo, ano_lancamento) VALUES ('Bem Sertanejo', 2014); -- ID 8
INSERT INTO album (titulo, ano_lancamento) VALUES ('Bem Sertanejo - O Show (Ao Vivo)', 2017); -- ID 9
INSERT INTO album (titulo, ano_lancamento) VALUES ('Bem Sertanejo - (1ª Temporada) - EP', 2014); -- ID 10

-- Guns N' Roses
INSERT INTO album (titulo, ano_lancamento) VALUES ('Use Your Illusion I', 1991); -- ID 11
INSERT INTO album (titulo, ano_lancamento) VALUES ('Use Your Illusion II', 1991); -- ID 12
INSERT INTO album (titulo, ano_lancamento) VALUES ('Greatest Hits', 2004); -- ID 13

-- Relacionamento N:N
INSERT INTO artista_album (artista_id, album_id) VALUES 
(1, 1), (1, 2), (1, 3), 
(2, 4), (2, 5), (2, 6), (2, 7), 
(3, 8), (3, 9), (3, 10), 
(4, 11), (4, 12), (4, 13);