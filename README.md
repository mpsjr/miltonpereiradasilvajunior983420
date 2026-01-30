# Desafio Técnico - Processo Seletivo SEPLAG/MT (Engenheiro da Computação)

## Dados do Candidato
* **Nome:** Milton Pereira da Silva Júnior
* **Vaga:** Analista de Tecnologia da Informação - Perfil Engenheiro da Computação (Back-End)
* **Email:** mpsjunior@gmail.com

## Sobre o Projeto
API REST desenvolvida para gerenciamento de Artistas e Álbuns musicais.

### Tecnologias Utilizadas
* **Linguagem:** Java 17
* **Framework:** Spring Boot 3
* **Banco de Dados:** PostgreSQL 15
* **Object Storage:** MinIO
* **Gerenciamento de Dados:** Flyway Migrations
* **Containerização:** Docker & Docker Compose
* **Documentação:** OpenAPI / Swagger

## Decisões Arquiteturais (Fase 1 & 2)
1. **Estrutura de Banco de Dados:**
   - Adotado relacionamento N:N entre `Artista` e `Album` conforme solicitado.
   - Utilização do **Flyway** para versionamento de schema e carga inicial de dados.
   - IDs autoincrementais (`BIGSERIAL`) para entidades de negócio.
   - Para o requisito de **Regionais** (Sincronização), optou-se por não usar autoincremento para permitir controle manual dos IDs externos.

2. **Infraestrutura:**
   - O projeto é entregue totalmente containerizado via `docker-compose`, orquestrando a API, o Banco de Dados e o MinIO.

## Como Executar (Ambiente de Desenvolvimento)

### Pré-requisitos
* Docker e Docker Compose instalados.
* Java 17 JDK e Maven instalados.

### Passos
