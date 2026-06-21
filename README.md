# CTBJJ Backend
    2
    3 API REST para gerenciamento de uma academia de Brazilian Jiu-Jitsu. Controla alunos, professores, horários de aulas e check-ins com suporte a QR Code.
    4
    5 ## Tecnologias
    6
    7 - **Java 21** + **Spring Boot 4**
    8 - **Spring Security** + **JWT** (jjwt 0.12)
    9 - **Spring Data JPA** + **PostgreSQL**
   10 - **Lombok**
   11 - **Maven**
   12
   13 ## Pré-requisitos
   14
   15 - Java 21+
   16 - PostgreSQL rodando na porta `5432`
   17 - Banco de dados `ctbjj` criado
   18
   19 ## Configuração
   20
   21 As configurações padrão estão em `src/main/resources/application.properties`:
   22
   23 ```properties
   24 spring.datasource.url=jdbc:postgresql://localhost:5432/ctbjj
   25 spring.datasource.username=postgres
   26 spring.datasource.password=ctbjj
   27
   28 jwt.secret=<chave-base64>
   29 jwt.expiration-ms=86400000
   30 ```
   31
   32 Para produção, gere uma chave JWT segura:
   33 ```bash
   34 openssl rand -base64 32
   35 ```
   36
   37 ## Como rodar
   38
   39 ```bash
   40 ./mvnw spring-boot:run
   41 ```
   42
   43 O Hibernate cria/atualiza as tabelas automaticamente (`ddl-auto=update`).
   44
   45 ## Autenticação
   46
   47 JWT via Bearer token. Inclua o header em todas as rotas protegidas:
   48
   49 ```
   50 Authorization: Bearer <token>
   51 ```
   52
   53 ### Roles
   54
   55 | Role | Descrição |
   56 |------|-----------|
   57 | `ADMIN` | Acesso total |
   58 | `PROFESSOR` | Gerencia horários e check-ins |
   59 | `STUDENT` | Acesso limitado aos próprios dados |
   60
   61 ## Endpoints
   62
   63 ### Auth — `/api/auth`
   64
   65 | Método | Rota | Acesso | Descrição |
   66 |--------|------|--------|-----------|
   67 | POST | `/register` | Público | Cadastro de novo usuário |
   68 | POST | `/login` | Público | Login, retorna JWT |
   69
   70 ### Alunos — `/api/students`
   71
   72 | Método | Rota | Acesso | Descrição |
   73 |--------|------|--------|-----------|
   74 | GET | `/` | ADMIN, PROFESSOR | Lista todos (paginado) |
   75 | GET | `/{id}` | ADMIN, PROFESSOR | Busca por ID |
   76 | GET | `/search?name=` | ADMIN, PROFESSOR | Busca por nome |
   77 | GET | `/me` | STUDENT | Dados do aluno logado |
   78 | POST | `/` | ADMIN | Cria aluno |
   79 | PUT | `/{id}` | ADMIN | Atualiza aluno |
   80 | PATCH | `/{id}/status` | ADMIN | Atualiza status de matrícula |
   81 | DELETE | `/{id}` | ADMIN | Remove aluno |
   82
   83 ### Professores — `/api/professors`
   84
   85 | Método | Rota | Acesso | Descrição |
   86 |--------|------|--------|-----------|
   87 | GET | `/` | Público | Lista todos |
   88 | GET | `/{id}` | Público | Busca por ID |
   89 | POST | `/` | ADMIN | Cria professor |
   90 | PUT | `/{id}` | ADMIN | Atualiza professor |
   91 | DELETE | `/{id}` | ADMIN | Remove professor |
   92
   93 ### Tipos de Aula — `/api/class-types`
   94
   95 | Método | Rota | Acesso | Descrição |
   96 |--------|------|--------|-----------|
   97 | GET | `/` | Público | Lista todos |
   98 | GET | `/{id}` | Público | Busca por ID |
   99 | POST | `/` | ADMIN | Cria tipo de aula |
  100 | PUT | `/{id}` | ADMIN | Atualiza tipo de aula |
   99 | POST | `/` | ADMIN | Cria tipo de aula |
  100 | PUT | `/{id}` | ADMIN | Atualiza tipo de aula |
  101 | DELETE | `/{id}` | ADMIN | Remove tipo de aula |
  102
  103 ### Horários — `/api/schedules`
  104
  105 | Método | Rota | Acesso | Descrição |
  106 |--------|------|--------|-----------|
  107 | GET | `/` | Público | Lista todos |
  108 | GET | `/today` | Público | Aulas do dia |
  109 | GET | `/{id}` | Público | Busca por ID |
  110 | POST | `/` | ADMIN, PROFESSOR | Cria horário |
  111 | PUT | `/{id}` | ADMIN, PROFESSOR | Atualiza horário |
  112 | DELETE | `/{id}` | ADMIN | Remove horário |
  113
  114 ### Check-ins — `/api/checkins`
  115
  116 | Método | Rota | Acesso | Descrição |
  117 |--------|------|--------|-----------|
  118 | POST | `/qr` | ADMIN, PROFESSOR, STUDENT | Check-in via QR Code |
  119 | POST | `/manual` | ADMIN, PROFESSOR | Check-in manual |
  120 | POST | `/{id}/checkout` | ADMIN, PROFESSOR | Registra saída |
  121 | GET | `/student/{studentId}/stats` | ADMIN, PROFESSOR, STUDENT | Estatísticas do aluno |
  122 | GET | `/student/{studentId}/history` | ADMIN, PROFESSOR, STUDENT | Histórico paginado |
  123
  124 ## Modelo de Dados
  125
  126 ### Faixas (`Belt`)
  127 `WHITE` · `BLUE` · `PURPLE` · `BROWN` · `BLACK`
  128
  129 ### Status de Matrícula (`EnrollmentStatus`)
  130 `ACTIVE` · `INACTIVE` · `SUSPENDED`
  131
  132 ### Método de Check-in (`CheckInMethod`)
  133 `QR_CODE` · `MANUAL`
  134
  135 ## Build
  136
  137 ```bash
  138 # Compilar e gerar JAR
  139 ./mvnw clean package
  140
  141 # Rodar o JAR gerado
  142 java -jar target/main-0.0.1-SNAPSHOT.jar
  143 ```
