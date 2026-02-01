# PROCESSO SELETIVO CONJUNTO Nº 001/2026/SEPLAG e demais Órgãos

## Dados do Processo Seletivo / Vaga
* **Cargo :** Analista de Tecnologia da Informação
* **Perfil:** Engenheiro da Computação (Sênior) 

## Dados do Candidato / Inscrição
* **N° Inscrição :** 16494
* **Nome         :** Milton Pereira da Silva Júnior
* **Email        :** mpsjunior@gmail.com

## Sobre o Projeto Profissional
 API REST desenvolvida para gerenciamento de Artistas, Álbuns musicais e integração de regionais.

## 🚀 Tecnologias Utilizadas
- **Java 17** (LTS)
- **Spring Boot 3.5.10** (Web, Validation, JPA, WebSocket)
- **PostgreSQL 15** (Banco de Dados Relacional)
- **MinIO** (Simula um ambiente AWS S3 real. As imagens não são salvas no banco, apenas suas referências)
- **Flyway** (Versionamento e Migração de Banco de Dados)
- **Docker & Docker Compose** (Orquestração de Ambiente)
- **Bucket4j** (Rate Limiting)
- **Swagger / OpenAPI** (Documentação)

## 📋 Funcionalidades Implementadas

### Core
- [x] **CRUD de Artistas**: Ordenação dinâmica e busca por nome.
- [x] **CRUD de Álbuns**: Paginação, relacionamento N:N com Artistas.
- [x] **Upload de Imagens**: Suporte a múltiplas capas por álbum, armazenadas no MinIO.
- [x] **Links Seguros**: Geração de URLs pré-assinadas (Presigned URLs) com expiração de 30 min.
- [x] **Ambiente Containerizado**: Setup completo orquestrado via Docker Compose (API + MinIO + BD).

### Sênior
- [x] **Sincronização de Regionais**: Integração com API externa, implementando lógica de versionamento (Inativar antigo vs Criar novo) para manter histórico.
- [x] **WebSockets**: Notificação em tempo real ao cadastrar novos álbuns (`/v1/albuns`), com painel de monitoramento visual.
- [x] **Rate Limiting**: Limitan requisições por IP, garantindo segurança contra DDoS/Brute-force e que a infraestrutura não seja sobrecarregada por acessos excessivos (10 requisições/minuto).

## Decisões Arquiteturais
1. **Estrutura de Banco de Dados:**
   - Adotado relacionamento N:N entre `Artista` e `Album` conforme solicitado.
   - Utilização do **Flyway** para versionamento de schema e carga inicial de dados.
   - Implementada carga inicial de dados de exemplos (Artistas e Álbuns).
   - IDs autoincrementais (`BIGSERIAL`) para entidades de negócio.
   - Versionamento de Regionais: A tabela regional utiliza um ID interno (id) diferente do ID externo (id_regional). 
      Isso permite que, se uma regional mudar de nome na API externa, o sistema inative o registro antigo e crie um novo, 
      mantendo a integridade referencial histórica.

2. **Infraestrutura:**
   - O projeto é entregue totalmente containerizado via `docker-compose`, orquestrando a API, o Banco de Dados e o MinIO.
   
3. **Segurança**
   - Implementado filtro de Rate Limit (10 requisições/min) para garantir segurança e disponibilidade do serviço.
   - Criada lista de prefixos que não consomem tokens do bucket (garantir que entidades confiáveis nunca sejam bloqueadas).


## 🛠️ Como Executar o Projeto

### Pré-requisitos
- Docker e Docker Compose instalados.
- JDK 17 e Maven instalados.

### Passo 1: Subir Infraestrutura
   Na raiz do projeto, execute:
      ``docker-compose up -d´´
      - Esse comando irá criar e iniciar todos os serviços definidos no arquivo docker-compose.yml de uma só vez.
      - Isso iniciará o PostgreSQL (Porta 5432) e o MinIO (Porta 9000/9001).
### Passo 2: Executar Aplicação
   Na raiz do projeto, execute:
      ``mvn spring-boot:run´´
      - Esse comando compila e executa rapidamente a aplicação Spring Boot diretamente do código-fonte.
      - A aplicação iniciará na porta 8080. O Flyway criará as tabelas e fará a carga inicial de dados automaticamente.
   
## 📚 Documentação da API (Swagger)
   Acesse a interface interativa para testar os endpoints: 👉 http://localhost:8080/swagger-ui.html

   Principais Endpoints de Teste:
      Listar Álbuns (Paginado) - Permite consultar álbuns por artista (cantor/banda).
         GET /v1/albuns
      
      Monitoramento em Tempo Real (WebSocket) - Permite testar a notificação ao cadastrar novos álbuns .
         Abra o navegador em: http://localhost:8080 (Painel de Monitoramento). Mantenha esta aba aberta.
         No Swagger, cadastre um novo álbum (POST /v1/albuns).
         Veja o alerta aparecer instantaneamente no Painel de Monitoramento.

      Upload de Capas - Permite enviar imagens para o MinIO e vincular ao álbum.
         POST /v1/albuns/{id}/capa (Use o ID do álbum)

      Disparar Sincronização de Regionais (Integração) - Busca dados da API externa e atualiza a base local.
         Execute GET /v1/regionais (Lista vazia ou desatualizada).
         Execute POST /v1/regionais/sincronizacao (Busca dados da API externa).
         Execute GET /v1/regionais novamente para ver os dados populados.

      Testar Rate Limit - Controle de requisições que um usuário pode fazer.
         Faça +10 requisições em 1 minuto para receber HTTP 429.