# WorkNest API

API REST de gestão de tarefas e workspaces colaborativas. Usuários podem criar workspaces, gerenciar tasks e adicionar colaboradores com controle de papéis.

## Tecnologias

- Java 21
- Spring Boot
- Spring Securityf + JWT
- PostgreSQL
- Spring Data JPA
- Swagger (SpringDoc OpenAPI)

---

## Regras de negócio

- O criador de uma workspace é automaticamente seu **Owner**
- Apenas o **Owner** pode criar, editar e deletar tasks
- O **Contributor** pode apenas alterar o status de uma task
- Um usuário não pode ser Contributor de uma workspace que ele mesmo criou
- Todas as rotas (exceto cadastro e login) exigem um token JWT válido no header da requisição

---

## Rotas

### Auth

| Método | Rota | Descrição | Autenticação |
|--------|------|-----------|--------------|
| POST | `/auth/register` | Cadastro de usuário | Não |
| POST | `/auth/login` | Login e geração do token JWT | Não |

#### POST `/auth/register`
```json
{
  "email": "usuario@email.com",
  "password": "minimo8caracteres",
  "passwordConfirmation": "minimo8caracteres"
}
```

#### POST `/auth/login`
```json
{
  "email": "usuario@email.com",
  "password": "minimo8caracteres"
}
```
**Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

---

### Workspaces

| Método | Rota | Descrição | Papel exigido |
|--------|------|-----------|---------------|
| POST | `/workspaces` | Criar workspace | Autenticado |
| GET | `/workspaces/owned` | Listar workspaces onde é Owner | Autenticado |
| GET | `/workspaces/contributing` | Listar workspaces onde é Contributor | Autenticado |
| GET | `/workspaces/{id}` | Buscar workspace por ID | Membro |
| PUT | `/workspaces/{id}` | Atualizar workspace | Owner |
| DELETE | `/workspaces/{id}` | Deletar workspace | Owner |
| POST | `/workspaces/{id}/contributor` | Adicionar colaborador | Owner |

#### POST `/workspaces`
```json
{
  "name": "Minha Workspace"
}
```

#### PUT `/workspaces/{id}`
```json
{
  "name": "Novo Nome"
}
```

#### POST `/workspaces/{id}/contributor`
```json
{
  "contributorEmail": "colaborador@email.com"
}
```

---

### Tasks

| Método | Rota | Descrição | Papel exigido |
|--------|------|-----------|---------------|
| POST | `/workspaces/{workspaceId}/tasks` | Criar task | Owner |
| PUT | `/workspaces/{workspaceId}/tasks/{taskId}` | Atualizar task | Owner |
| PATCH | `/workspaces/{workspaceId}/tasks/{taskId}/status` | Atualizar status da task | Owner ou Contributor |
| DELETE | `/workspaces/{workspaceId}/tasks/{taskId}` | Deletar task | Owner |

#### POST `/workspaces/{workspaceId}/tasks`
```json
{
  "title": "Nome da task",
  "description": "Descrição da task"
}
```

#### PUT `/workspaces/{workspaceId}/tasks/{taskId}`
```json
{
  "title": "Novo título",
  "description": "Nova descrição"
}
```

#### PATCH `/workspaces/{workspaceId}/tasks/{taskId}/status`
```json
{
  "status": "IN_PROGRESS"
}
```

> Status disponíveis: `TODO`, `IN_PROGRESS`, `DONE`

---

## Autenticação

Todas as rotas protegidas exigem o token JWT no header:

```
Authorization: Bearer {token}
```
