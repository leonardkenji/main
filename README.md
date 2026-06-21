# CTBJJ Backend

API REST para gerenciamento de uma academia de Brazilian Jiu-Jitsu. Controla alunos, professores, horários de aulas e check-ins com suporte a QR Code.

## Tecnologias

- **Java 21** + **Spring Boot 4**
- **Spring Security** + **JWT** (jjwt 0.12)
- **Spring Data JPA** + **PostgreSQL**
- **Lombok**
- **Maven**

## Pré-requisitos

- Java 21+
- PostgreSQL rodando na porta `5432`
- Banco de dados `ctbjj` criado

## Configuração

As configurações padrão estão em `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ctbjj
spring.datasource.username=postgres
spring.datasource.password=ctbjj

jwt.secret=<chave-base64>
jwt.expiration-ms=86400000
```

Para produção, gere uma chave JWT segura:
```bash
openssl rand -base64 32
```

## Como rodar

```bash
./mvnw spring-boot:run
```

O Hibernate cria/atualiza as tabelas automaticamente (`ddl-auto=update`).

## Autenticação

JWT via Bearer token. Inclua o header em todas as rotas protegidas:

```
Authorization: Bearer <token>
```

### Roles

| Role | Descrição |
|------|-----------|
| `ADMIN` | Acesso total |
| `PROFESSOR` | Gerencia horários e check-ins |
| `STUDENT` | Acesso limitado aos próprios dados |

## Endpoints

### Auth — `/api/auth`

| Método | Rota | Acesso | Descrição |
|--------|------|--------|-----------|
| POST | `/register` | Público | Cadastro de novo usuário |
| POST | `/login` | Público | Login, retorna JWT |

### Alunos — `/api/students`

| Método | Rota | Acesso | Descrição |
|--------|------|--------|-----------|
| GET | `/` | ADMIN, PROFESSOR | Lista todos (paginado) |
| GET | `/{id}` | ADMIN, PROFESSOR | Busca por ID |
| GET | `/search?name=` | ADMIN, PROFESSOR | Busca por nome |
| GET | `/me` | STUDENT | Dados do aluno logado |
| POST | `/` | ADMIN | Cria aluno |
| PUT | `/{id}` | ADMIN | Atualiza aluno |
| PATCH | `/{id}/status` | ADMIN | Atualiza status de matrícula |
| DELETE | `/{id}` | ADMIN | Remove aluno |

### Professores — `/api/professors`

| Método | Rota | Acesso | Descrição |
|--------|------|--------|-----------|
| GET | `/` | Público | Lista todos |
| GET | `/{id}` | Público | Busca por ID |
| POST | `/` | ADMIN | Cria professor |
| PUT | `/{id}` | ADMIN | Atualiza professor |
| DELETE | `/{id}` | ADMIN | Remove professor |

### Tipos de Aula — `/api/class-types`

| Método | Rota | Acesso | Descrição |
|--------|------|--------|-----------|
| GET | `/` | Público | Lista todos |
| GET | `/{id}` | Público | Busca por ID |
| POST | `/` | ADMIN | Cria tipo de aula |
| PUT | `/{id}` | ADMIN | Atualiza tipo de aula |
| DELETE | `/{id}` | ADMIN | Remove tipo de aula |

### Horários — `/api/schedules`

| Método | Rota | Acesso | Descrição |
|--------|------|--------|-----------|
| GET | `/` | Público | Lista todos |
| GET | `/today` | Público | Aulas do dia |
| GET | `/{id}` | Público | Busca por ID |
| POST | `/` | ADMIN, PROFESSOR | Cria horário |
| PUT | `/{id}` | ADMIN, PROFESSOR | Atualiza horário |
| DELETE | `/{id}` | ADMIN | Remove horário |

### Check-ins — `/api/checkins`

| Método | Rota | Acesso | Descrição |
|--------|------|--------|-----------|
| POST | `/qr` | ADMIN, PROFESSOR, STUDENT | Check-in via QR Code |
| POST | `/manual` | ADMIN, PROFESSOR | Check-in manual |
| POST | `/{id}/checkout` | ADMIN, PROFESSOR | Registra saída |
| GET | `/student/{studentId}/stats` | ADMIN, PROFESSOR, STUDENT | Estatísticas do aluno |
| GET | `/student/{studentId}/history` | ADMIN, PROFESSOR, STUDENT | Histórico paginado |

## Modelo de Dados

### Faixas (`Belt`)
`WHITE` · `BLUE` · `PURPLE` · `BROWN` · `BLACK`

### Status de Matrícula (`EnrollmentStatus`)
`ACTIVE` · `INACTIVE` · `SUSPENDED`

### Método de Check-in (`CheckInMethod`)
`QR_CODE` · `MANUAL`

## Build

```bash
# Compilar e gerar JAR
./mvnw clean package

# Rodar o JAR gerado
java -jar target/main-0.0.1-SNAPSHOT.jar
```
