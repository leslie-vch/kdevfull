# Evaluación Técnica – Perfil Semi Senior Fullstack (Java + Spring Boot | React + Next.js)

¡Bienvenido a la evaluación técnica de **Desarrollador Fullstack**! para Kruger Corporation  
Este ejercicio tiene como objetivo conocer tus habilidades en desarrollo backend y frontend, utilizando buenas prácticas de desarrollo de software.

---

## Parte 1 – Backend (Java + Spring Boot)

Construir una API RESTful bajo el contexto `/kfullstack` que permita gestionar:

- Usuarios
- Proyectos
- Tareas

### Requisitos funcionales

#### 1. Autenticación y autorización
- Registro y login con JWT
- Roles: `ADMIN`, `USER`
- Solo `ADMIN` puede crear usuarios
- Usuarios autenticados pueden acceder a sus propios recursos

#### 2. Endpoints requeridos (prefijo `/kfullstack`)
##### Usuarios (ADMIN)
- `POST /kfullstack/users`: Crear usuario
- `GET /kfullstack/users`: Listar usuarios
- `GET /kfullstack/users/{id}`: Ver usuario

##### Proyectos
- `POST /kfullstack/projects`: Crear proyecto
- `GET /kfullstack/projects`: Listar proyectos del usuario autenticado
- `PUT /kfullstack/projects/{id}`: Editar proyecto
- `DELETE /kfullstack/projects/{id}`: Eliminar proyecto

##### Tareas
- `POST /kfullstack/tasks`: Crear tarea (asociada a un proyecto y usuario)
- `GET /kfullstack/tasks`: Listar tareas del usuario
- `GET /kfullstack/tasks/project/{projectId}`: Tareas de un proyecto
- `PUT /kfullstack/tasks/{id}`: Editar tarea
- `DELETE /kfullstack/tasks/{id}`: Eliminar tarea

### Entidades mínimas

#### `User`
- id, username, email, password (encriptada), role (USER, ADMIN)

#### `Project`
- id, name, description, createdAt, owner (User)

#### `Task`
- id, title, description, status (PENDING, IN_PROGRESS, DONE), assignedTo (User), project (Project), dueDate, createdAt

### Requisitos técnicos

- Spring Boot 3
- Spring Data JPA (H2 o PostgreSQL)
- Spring Security con JWT
- Validaciones con javax.validation
- Arquitectura en capas (`controller`, `service`, `repository`, `dto`, `mapper`)
- Swagger u OpenAPI para documentación
- Manejo de errores global con `@ControllerAdvice`

### Extras valorados

- Auditoría (`createdBy`, `updatedBy`)
- Logs estructurados
- Tests con JUnit y Mockito
- Código limpio y modular

---

## Parte 2 – Frontend (React + Next.js)

### Objetivo

Construir una aplicación web que consuma la API desarrollada y permita al usuario autenticado gestionar sus proyectos y tareas.

### Requisitos funcionales

#### Autenticación
- Login y almacenamiento de JWT (localStorage o cookie segura)
- Redirección protegida según sesión

#### Pantallas
1. **Login**
2. **Dashboard**
   - Lista de proyectos del usuario
   - Crear/editar/eliminar proyecto
3. **Detalle del Proyecto**
   - Listar tareas del proyecto
   - Crear/editar/eliminar tareas
   - Cambiar estado (DONE, IN_PROGRESS, PENDING.)
   - Asignar tareas a usuarios existentes
4. **Filtros de tareas**
   - Filtrar por estado (PENDING, etc.)
   - Ordenar por fecha de vencimiento

### Requisitos técnicos

- Next.js 13+ (App Router)
- React 18
- Axios o fetch para consumir API
- Autenticación basada en token
- Manejo de estado: Zustand o Context
- Estilos con TailwindCSS o Styled Components
- Estructura modular y reutilizable

### Extras valorados

- Skeleton loaders, spinners y mensajes vacíos
- Responsive Design
- Validaciones en formularios
- Pruebas con Testing Library
- Linter + Prettier configurado

---

## Entrega

- Sube tu solución a un repositorio público (GitHub, GitLab, Bitbucket).
- Usa una carpeta para backend y otra para frontend.
- Incluye un `README.md` con:
  - Instrucciones para ejecutar backend y frontend
  - Credenciales preconfiguradas para probar
  - Notas adicionales si lo consideras necesario

---

## Evaluación (Criterios)

| Criterio                            | Puntos |
|------------------------------------|--------|
| Cumplimiento funcional             | 20     |
| Buenas prácticas y arquitectura    | 20     |
| Seguridad y autenticación          | 15     |
| Validaciones y errores             | 10     |
| Código limpio, modular, comentado  | 10     |
| Pruebas (unitarias o integración)  | 10     |
| UI/UX (frontend)                   | 10     |
| Documentación y despliegue         | 5      |

---

¡Gracias por participar y suerte en tu evaluación!
