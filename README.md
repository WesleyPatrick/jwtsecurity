# JWT Security API

API REST de autenticação e autorização desenvolvida com **Spring Boot**, utilizando **JWT (Access Token + Refresh Token)**, **Spring Security**, **Flyway** e **PostgreSQL**.

Este projeto foi construído como uma base sólida para aplicações stateless, com controle de acesso por roles e práticas comuns em ambientes profissionais.

---

## Stack

- Java 17  
- Spring Boot  
- Spring Security  
- JWT (Access + Refresh Token)  
- Spring Data JPA  
- PostgreSQL  
- Flyway  
- Bean Validation  
- OpenAPI / Swagger (springdoc)

---

## Funcionalidades

- Registro de usuários
- Login com geração de access token e refresh token
- Renovação de access token
- Logout com revogação de refresh token
- Autorização baseada em roles (`USER`, `ADMIN`)
- Segurança stateless (sem sessão)
- Migrations versionadas com Flyway
- Seed de dados por ambiente
- Documentação automática via Swagger

---

## Autenticação

### Access Token
- JWT de curta duração
- Enviado no header:
```
Authorization: Bearer <access_token>
```

### Refresh Token
- JWT de longa duração
- Armazenado no banco de forma segura (hash)
- Usado apenas para gerar um novo access token
- Revogado no logout

---

## Endpoints

### Auth

| Método | Endpoint | Descrição |
|------|---------|----------|
| POST | `/auth/register` | Criação de usuário |
| POST | `/auth/login` | Autenticação |
| POST | `/auth/refresh` | Renovação do access token |
| POST | `/auth/logout` | Logout |

### Testes de autorização

| Método | Endpoint | Permissão |
|------|---------|----------|
| GET | `/test/user` | ROLE_USER |
| GET | `/test/admin` | ROLE_ADMIN |
| GET | `/test` | Usuário autenticado |
| GET | `/test/whoami` | Usuário autenticado |

---

## Swagger / OpenAPI

Acesse:
```
http://localhost:8080/swagger-ui/index.html
```

---

## Banco de dados e migrations

- PostgreSQL
- Flyway para versionamento de schema
- Migrations automáticas

---

## Autor

Wesley Patrick  
Backend / Full Stack Developer
