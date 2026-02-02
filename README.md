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
- [x] **WebSockets**: Notificação em tempo real ao cadastrar novos álbuns (`/v1/albuns`), com painel de monitoramento visual (`http://localhost:8080/index.html`).
- [x] **Rate Limiting**: Limita requisições por IP (10 requisições/minuto), garantindo que a infraestrutura não seja sobrecarregada por acessos excessivos e segurança conta de ataques de força bruta.
- [x] **Endpoint de Regionais**: Integração com API externa, implementando lógica de versionamento (Inativar antigo vs Criar novo) para manter histórico.

## 🏗️ Decisões Arquiteturais
1. **Estrutura de Banco de Dados:**
   - IDs autoincrementais (`bigserial`).
   - Adotado relacionamento N:N entre `Artista` e `Álbum`. Criada tabela `artista_album` para fazer o relacionamento.
   - Criada tabela `imagem_album` para salvar o vinculo entre um álbum e suas várias capas salvas no MinIO.
   - Utilização do **Flyway Migrations** para versionamento de schema e carga inicial de dados de exemplo (artistas e álbuns).
   - **Versionamento de Regionais**: A tabela `regional` utiliza um ID interno (id) diferente do ID externo (id_regional). 
      Isso permite que, se uma regional mudar de nome na API externa, o sistema inative o registro antigo e crie um novo, 
      mantendo a integridade referencial histórica.

2. **Infraestrutura:**
   - O projeto utiliza `docker-compose` para orquestrar dependências vitais (Banco e Storage).
   - **Healthchecks (Liveness/Readiness)** configurados para garantir que o banco e storage estejam prontos antes da conexão.
   
3. **Segurança e Performance**
   - Implementado filtro de **Rate Limit** (10 requisições/min) via Bucket4j, para garantir segurança e disponibilidade do serviço.
   - Criada Whitelist de prefixos que não consomem tokens do bucket (para permitir acesso irrestrito à documentação e arquivos estáticos).

## 📸 Screenshots

### 1. Modelagem de Dados
Estrutura relacional do banco de dados (PostgreSQL).  
![Diagrama ](assets/diagrama-db.png)

### 2. Armazenamento de capas (MinIO)
As capas dos álbuns são armazenadas em buckets no MinIO.  
![Dashboard MinIO](assets/minio-dashboard.png)

### 3. Monitoramento em Tempo Real (WebSocket)
![Monitoramento WebSocket - Aguardando](assets/monitor-websocket_aguardando.png).  
O sistema notifica todos os clientes conectados quando um novo álbum é cadastrado.  
![Monitoramento WebSocket - Mensagem](assets/monitor-websocket_mensagem.png).

---

## 🛠️ Como Executar o Projeto
> **Nota:** Siga os passos abaixo para garantir que não haja conflito de portas.
### Pré-requisitos
- **Docker e Docker Compose** instalados.
- **JDK 17 e Maven** instalados.
- **Portas Livres:** Certifique-se de que não há nada rodando nas portas **8080**, **5432** e **9000**.


### Passo 1: Subir Infraestrutura (Banco e MinIO)
- No terminal, na raiz do projeto, execute:  
   `docker-compose up -d postgres minio`  
   _ Isso iniciará o PostgreSQL (Porta 5432) e o MinIO (Porta 9000/9001), sem ocupar a porta 8080 (usada pela API).  
   
- Ainda no terminal, execute:  
   `docker ps`  
   _ Esse comando lista os containers que estão rodando.  
 
### Passo 2: Executar Aplicação
- No terminal, na raiz do projeto, execute:  
   `mvn spring-boot:run`  
   _ Com a infraestrutura rodando, executamos a API via Maven.  
   _ A aplicação iniciará na porta 8080. O Flyway criará as tabelas e fará a carga inicial de dados automaticamente.  


> ### **Comandos que podem auxiliar:**  
> `docker-compose down`            Remove os containers criados pelo docker-compose (reiniciar o ambiente do zero).  
> `docker-compose down -v`         Remove containers e os volumes (Apaga BD, histórico do Flyway e dados persistidos).  
> `docker-compose up -d`           Sobe os containers definidos no docker-compose.yml (API, PostgreSQL, MinIO, etc.).  
> `docker ps`                      Lista containers em execução (Verificar se containers estão rodando).  
> `.\mvnw spring-boot:run`         Usa o Maven Wrapper do projeto. Compila e executa a aplicação Spring Boot.  
> `.\mvnw clean spring-boot:run`   Limpa clases antigas, compila e executa a aplicação Spring Boot.  
> `taskkill /F /IM java.exe`       Mata todos os processo java rodando (em caso de erro de porta ao subir aplicação).  

---


## 📚 Guia de Testes - Documentação da API (Swagger)
Acesse a interface do Swagger para testar todos os endpoints de forma interativa:  
👉 **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

### 🎤 Artistas - Gerenciamento de cantores e bandas.

- **Busca artista por Id** - Permite consultar um artista informando o seu Id.  
  `GET /v1/artistas/{id}`

- **Listar artistas** - Permite buscar artistas por nome (opcional) com ordenação alfabética (asc/desc).  
  `GET /v1/artistas`
         
- **Atualizar artista existente** - Permite atualizar dados do artista.  
  `PUT /v1/artistas/{id}`
      
- **Cadastrar novo artista** - Permite cadastrar os dados de um novo artista.  
  `POST /v1/artistas`

### 💿 Álbuns - Gerenciamento de Discos/Álbuns e relação com Artistas.
   
- **Busca álbum por Id** - Permite consultar um álbum informando o seu Id.  
  `GET /v1/albuns/{id}`

- **Listar Álbuns (Paginado)** - Permite consultar álbuns por artista (cantor/banda), ordenar a busca por um campo (sort), paginar a busca (page/size).  
  `GET /v1/albuns`
      
- **Atualizar Álbum** - Permite atualizar as informações de um álbum existente.  
  `PUT /v1/albuns/{id}`
   
- **Cadastrar novo Álbum** - Permite cadastrar um novo álbum e vincular a um artista.  
  `POST /v1/albuns`  

- 📡 **Monitor WebSocket** - Permite testar o monitoramento em tempo real ao cadastrar um novo álbum.  
   1 . Antes de cadastrar um novo álbum, abra o navegador em: [http://localhost:8080](http://localhost:8080) (Painel de Monitoramento).  
   2 . Verifique se o status inicial é "Conectado".  
   3 . Mantenha essa aba visível ou em uma janela separada lado-a-lado.  
   4 . Efetue o cadastro de um novo álbum em `POST /v1/albuns`. 

- **Upload de capa do Álbum** - Permite fazer o upload de imagens que serão as capas do Álbum. A imagem é enviada para o MinIO.  
  `POST /v1/albuns/{id}/capa`

### 🗺️ Regionais - Sincronização com API externa.

- **Listar Regionais ativas** - Permite consultar as Regionais que estão ativas.  
  `GET /v1/regionais`
  > **Resultado:** Ao executar a consulta antes da sincronização, irá mostrar uma lista vazia.  

- **Disparar Sincronização** - Acessa a API externa e busca dados para atualizar a base de dados local.  
  `POST /v1/regionais/sincronizacao`  
  > **Resultado:** Ao finalizar a sincronização, o sistema informa o resultado com o número de regionais inseridas, atualizadas e inativadas.
         
### 🚦 Rate Limit - Controle de requisições que um usuário pode fazer.

- **Testar Rate Limit**
  1. Em qualquer endpoint (ex: `GET /v1/artistas`).
  2. Clique em "Execute" rapidamente (mais de 10 vezes em 1 minuto).
  3. Você receberá um erro HTTP 429 com a seguinte mensagem: *Limite de requisições excedido (10 req/min). Aguarde um momento.*