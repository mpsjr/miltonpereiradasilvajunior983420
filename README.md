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
   O projeto foi estruturado para atender requisitos de níveis Sênior, focando em escalabilidade, documentação e boas práticas.

## 🚀 Tecnologias Utilizadas
- **Java 17** (LTS)
- **Spring Boot 3.5.10** (Web, Validation, JPA, WebSocket)
- **PostgreSQL 15** (Banco de Dados Relacional)
- **MinIO** (Simula um ambiente AWS S3 real). As imagens não são salvas no banco, apenas suas referências)
- **Flyway** (Versionamento e Migração de Banco de Dados)
- **Docker & Docker Compose** (Orquestração de Ambiente)
- **Bucket4j** (Rate Limiting)
- **Swagger / OpenAPI** (Documentação)

## 📋 Funcionalidades Implementadas

### Requisitos Gerais
- [ ] **Segurança**: bloquear acesso ao endpoint a partir de domínios fora do domínio do serviço.
- [ ] **Autenticação JWT**: Com expiração a cada 5 minutos e possibilidade de renovação.
- [x] **CRUD de Artistas**: Ordenação dinâmica e busca por nome.
- [x] **CRUD de Álbuns**: Paginação, relacionamento N:N com Artistas.
- [x] **Upload de Imagens**: Suporte a múltiplas capas por álbum, armazenadas no MinIO.
- [x] **Links Seguros**: Geração de URLs pré-assinadas (Presigned URLs) com expiração de 30 min.
- [x] **Ambiente Containerizado**: Setup via Docker Compose (API + MinIO + BD).

### Requisitos Sênior
- [x] **Health Checks e Liveness/Readiness**: Implementado no arquivo docker-compose.yaml para monitorar a integridade da API, BD e MinIO.
- [ ] **Testes unitários**:.
- [x] **WebSockets**: Notificação em tempo real ao cadastrar novos álbuns (`/v1/albuns`), com painel de monitoramento visual (`index.html`).
- [x] **Rate Limiting**: Limita requisições por IP (10 requisições/minuto), garantindo que a infraestrutura não seja sobrecarregada por acessos excessivos e segurança contra ataques de força bruta.
- [x] **Endpoint de Regionais**: Integração com API externa, implementando lógica de versionamento (Inativar antigo vs Criar novo) para manter histórico.

## 🏗️ Decisões Arquiteturais
1. **Estrutura de Banco de Dados:**
   - IDs autoincrementais (`BIGSERIAL`).
   - Adotado relacionamento N:N entre `Artista` e `Album`. Criada tabela `artista_album` para fazer o relacionamento.
   - Criada tabela `imagem_album` para salvar o vinculo entre um álbum e suas várias capas salva no MinIO.
   - Utilização do **Flyway** para versionamento de schema e carga inicial de dados de exemplo (artistas e álbuns).
   - **Versionamento de Regionais**: A tabela `regional` utiliza um ID interno (id) diferente do ID externo (id_regional). 
      Isso permite que, se uma regional mudar de nome na API externa, o sistema inative o registro antigo e crie um novo, 
      mantendo a integridade referencial histórica.

2. **Modelagem de Dados (Diagrama ER):**
```mermaid
erDiagram
    ARTISTA ||--|{ ALBUM_ARTISTA : possui
    ALBUM ||--|{ ALBUM_ARTISTA : contem
    ALBUM {
        bigint id PK
        string titulo
        int ano_lancamento
    }
    ARTISTA {
        bigint id PK
        string nome
    }
    ARTISTA_ALBUM {
        bigint artista_id FK
        bigint album_id FK
    }
    REGIONAL {
        bigint id PK
        int id_regional_externo
        string nome
        boolean ativo
    }```

3. **Infraestrutura:**
   - O projeto utiliza `docker-compose` para orquestrar dependências vitais (Banco e Storage).
   - Healthchecks configurados para garantir que o banco esteja pronto antes da conexão.
   
4. **Segurança e Performance**
   - Implementado filtro de **Rate Limit** (10 requisições/min) via Bucket4j, para garantir segurança e disponibilidade do serviço.
   - Criada Whitelist de prefixos que não consomem tokens do bucket (permitir acesso irrestrito à documentação (Swagger) e arquivos estáticos).

---

## 🛠️ Como Executar o Projeto
> **Nota:** Siga os passos abaixo para garantir que não haja conflito de portas.
### Pré-requisitos
- Docker e Docker Compose instalados.
- JDK 17 e Maven instalados.
- **Portas Livres:** Certifique-se de que não há nada rodando na porta **8080**, **5432** e **9000**.


### Passo 1: Subir Infraestrutura (Banco e MinIO)
   No terminal, na raiz do projeto, execute:
      ``docker-compose up -d postgres minio´´
      - Isso iniciará o PostgreSQL (Porta 5432) e o MinIO (Porta 9000/9001), sem ocupar a porta 8080 (usada pela API)
   Ainda no terminal, execute:
      ``docker ps´´
      - Esse comando lista os containers que estão rodando.
      
### Passo 2: Executar Aplicação
   No terminal, na raiz do projeto, execute:
      ``mvn spring-boot:run´´
      - Com a infraestrutura rodando, executamos a API via Maven.
      - A aplicação iniciará na porta 8080. O Flyway criará as tabelas e fará a carga inicial de dados automaticamente.
   
## 📚 Guia de Testes - Documentação da API (Swagger)
   Acesse a interface do Swagger para testar os endpoints:
   👉 http://localhost:8080/swagger-ui.html

      #- Artistas (Gerenciamento de cantores e bandas):
         **Busca artista por Id** - Permite consultar um artista informando o seu Id.
            GET /v1/artistas/{id}
 
         **Listar artistas** - Permite buscar artistas por nome (opcional) com ordenação alfabética (asc/desc).
            GET /v1/artistas
            
         **Atualizar artista existente** - Permite atualizar dados do artista.
            PUT /v1/artistas/{id}
         
         **Cadastrar novo artista** - Permite cadastrar os dados de um novo artista.
            POST /v1/artistas

      #- Álbuns (Gerenciamento de Discos/Álbuns e relação com Artistas):
         **Busca álbum por Id** - Permite consultar um álbum informando o seu Id.
            GET /v1/albuns/{id}

         **Listar Álbuns (Paginado)** - Permite consultar álbuns por artista (cantor/banda), ordernar a busca por um campo (sort), paginar a busca (page/size).
            GET /v1/albuns
         
         **Atualizar Álbum** - Permite atualizar as informações de um álbum existente.
            PUT /v1/albuns/{id}

         **Monitor WebSocket** - Permite testar o monitoramento em tempo real ao cadastrar um novo álbum.
            Antes de cadastrar um novo álbum, abra o navegador em: http://localhost:8080 (Painel de Monitoramento). 
            Verifique se o status inicial é "Conectado".
            Mantenha essa aba visível ou em uma janela separada lado-a-lado.
            Efetue o cadastro de um novo álbum em "POST /v1/albuns"

         **Cadastrar novo Álbum** - Permite cadastrar um novo álbum e vincular a um artista.
            POST /v1/albuns
            Resultado: Olhe para a aba do Monitor WebSocket (teste anterior). Um alerta visual deve aparecer informando o novo cadastro.
      
         **Upload de capa do Álbum** - Permite fazer o upload imagens que serão as capas do Álbum. A imagem é enviada para o MinIO.
            POST /v1/albuns/{id}/capa
     
      #- Regionais (Sincornização com API externa):
         **Listar Regionais ativas** - Permite consultar as Regionais que estão ativas.
            GET /v1/regionais
            Resultado: Ao executar a consulta antes da sincronização, irá mostrar uma lista vazia.
   
         **Disparar Sincronização** - Acessa a API externa e busca dados para atualizar a base de dados local.
            POST /v1/regionais/sincronizacao (Busca dados da API externa).
            Resultado: Ao finalizar a sincronização, o sistema informa o resultado com o número de regionais inseridas, atualizadas e inativadas.  
            
      #- Rate Limit (ontrole de requisições que um usuário pode fazer):
         **Testar Rate Limit**
            Em qualquer endpoint (ex: GET /v1/artistas).
            Clique em "Execute" rapidamente (mais de 10 vezes em 1 minuto).
            Você receberá um erro HTTP 429 com a seguinte mensagem: `Limite de requisições excedido (10 req/min). Aguarde um momento.´.
