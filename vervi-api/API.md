# VerviApp API Documentation

Base URL: `http://localhost:3000/api`

## Table of Contents
- [Auth](#auth)
- [Users](#users)
- [Categories](#categories)
- [Requests](#requests)
- [Services](#services)
- [Service Applications](#service-applications)
- [Reviews](#reviews)
- [Chat](#chat)
- [Notifications](#notifications)

---

## Auth

### Register
Registra un nuevo usuario en la plataforma.

**Endpoint:** `POST /auth/register`

**Body:**
```json
{
  "email": "string (requerido, email válido)",
  "password": "string (requerido)",
  "name": "string (requerido)",
  "bio": "string (opcional)",
  "location": "string (opcional)",
  "photoUrl": "string (opcional)",
  "rating": "number (opcional)",
  "reviewCount": "number (opcional)",
  "suggestedPriceCop": "number (opcional)",
  "isProvider": "boolean (opcional, default: false)",
  "isOnline": "boolean (opcional, default: false)",
  "categoryIds": "number[] (opcional)"
}
```

**Respuesta Exitosa (201):**
```json
{
  "id": 1,
  "email": "usuario@example.com",
  "name": "Juan Pérez",
  "isProvider": false,
  "createdAt": "2026-05-06T10:00:00.000Z"
}
```

**Ejemplo:**
```bash
curl -X POST http://localhost:3000/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "juan@example.com",
    "password": "miPassword123",
    "name": "Juan Pérez",
    "location": "Bogotá, Colombia"
  }'
```

---

### Login
Inicia sesión y devuelve un token JWT.

**Endpoint:** `POST /auth/login`

**Body:**
```json
{
  "email": "string (requerido, email válido)",
  "password": "string (requerido)"
}
```

**Respuesta Exitosa (200):**
```json
{
  "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "email": "usuario@example.com",
    "name": "Juan Pérez"
  }
}
```

**Ejemplo:**
```bash
curl -X POST http://localhost:3000/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "juan@example.com",
    "password": "miPassword123"
  }'
```

---

## Users

### Get All Users
Obtiene todos los usuarios.

**Endpoint:** `GET /users`

**Respuesta Exitosa (200):**
```json
[
  {
    "id": 1,
    "email": "juan@example.com",
    "name": "Juan Pérez",
    "bio": "Electricista profesional",
    "location": "Bogotá, Chapinero",
    "photoUrl": "https://example.com/photo.jpg",
    "rating": 4.5,
    "reviewCount": 12,
    "suggestedPriceCop": 50000,
    "isProvider": true,
    "isOnline": true,
    "projectCount": 8,
    "requestCount": 3,
    "createdAt": "2026-01-15T08:00:00.000Z"
  }
]
```

**Ejemplo:**
```bash
curl -X GET http://localhost:3000/api/users
```

---

### Get User by ID
Obtiene un usuario específico por su ID.

**Endpoint:** `GET /users/:id`

**Parámetros:**
| Nombre | Tipo | Descripción |
|--------|------|-------------|
| id | number | ID del usuario |

**Respuesta Exitosa (200):**
```json
{
  "id": 1,
  "email": "juan@example.com",
  "name": "Juan Pérez",
  "bio": "Electricista con 10 años de experiencia",
  "location": "Bogotá, Chapinero",
  "photoUrl": "https://example.com/photo.jpg",
  "rating": 4.8,
  "reviewCount": 25,
  "suggestedPriceCop": 80000,
  "isProvider": true,
  "isOnline": true,
  "projectCount": 15,
  "requestCount": 5
}
```

**Ejemplo:**
```bash
curl -X GET http://localhost:3000/api/users/1
```

---

### Get My Profile
Obtiene el perfil del usuario autenticado (requiere JWT).

**Endpoint:** `GET /users/profile/me`

**Headers:**
```
Authorization: Bearer <access_token>
```

**Respuesta Exitosa (200):**
```json
{
  "id": 1,
  "email": "juan@example.com",
  "name": "Juan Pérez",
  "bio": "Electricista profesional",
  "location": "Bogotá, Chapinero",
  "photoUrl": "https://example.com/photo.jpg",
  "rating": 4.5,
  "isProvider": true,
  "isOnline": true
}
```

**Ejemplo:**
```bash
curl -X GET http://localhost:3000/api/users/profile/me \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

---

### Create User
Crea un nuevo usuario (duplicado de register).

**Endpoint:** `POST /users`

**Body:** Ver [Register](#register)

**Ejemplo:**
```bash
curl -X POST http://localhost:3000/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "nuevo@example.com",
    "password": "password123",
    "name": "Nuevo Usuario",
    "isProvider": true
  }'
```

---

### Update User
Actualiza un usuario existente.

**Endpoint:** `PUT /users/:id`

**Parámetros:**
| Nombre | Tipo | Descripción |
|--------|------|-------------|
| id | number | ID del usuario |

**Body (parcial):**
```json
{
  "name": "string (opcional)",
  "bio": "string (opcional)",
  "location": "string (opcional)",
  "photoUrl": "string (opcional)",
  "isProvider": "boolean (opcional)",
  "suggestedPriceCop": "number (opcional)",
  "categoryIds": "number[] (opcional)"
}
```

**Respuesta Exitosa (200):**
```json
{
  "id": 1,
  "name": "Juan Actualizado",
  "bio": "Nueva biografía",
  "updatedAt": "2026-05-06T12:00:00.000Z"
}
```

**Ejemplo:**
```bash
curl -X PUT http://localhost:3000/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Juan Actualizado",
    "bio": "Electricista con más experiencia",
    "photoUrl": "https://example.com/new-photo.jpg"
  }'
```

---

### Delete User
Elimina un usuario.

**Endpoint:** `DELETE /users/:id`

**Parámetros:**
| Nombre | Tipo | Descripción |
|--------|------|-------------|
| id | number | ID del usuario |

**Respuesta Exitosa (200):**
```json
{
  "message": "User deleted successfully"
}
```

**Ejemplo:**
```bash
curl -X DELETE http://localhost:3000/api/users/1
```

---

## Categories

### Get All Categories
Obtiene todas las categorías de servicios.

**Endpoint:** `GET /categories`

**Respuesta Exitosa (200):**
```json
[
  {
    "id": 1,
    "name": "Electricidad",
    "createdAt": "2026-01-01T00:00:00.000Z"
  },
  {
    "id": 2,
    "name": "Plomería",
    "createdAt": "2026-01-01T00:00:00.000Z"
  },
  {
    "id": 3,
    "name": "Carpintería",
    "createdAt": "2026-01-01T00:00:00.000Z"
  }
]
```

**Ejemplo:**
```bash
curl -X GET http://localhost:3000/api/categories
```

---

### Get Category by ID
Obtiene una categoría específica.

**Endpoint:** `GET /categories/:id`

**Parámetros:**
| Nombre | Tipo | Descripción |
|--------|------|-------------|
| id | number | ID de la categoría |

**Respuesta Exitosa (200):**
```json
{
  "id": 1,
  "name": "Electricidad",
  "createdAt": "2026-01-01T00:00:00.000Z"
}
```

**Ejemplo:**
```bash
curl -X GET http://localhost:3000/api/categories/1
```

---

### Create Category
Crea una nueva categoría.

**Endpoint:** `POST /categories`

**Body:**
```json
{
  "name": "string (requerido)"
}
```

**Respuesta Exitosa (201):**
```json
{
  "id": 4,
  "name": "Jardinería",
  "createdAt": "2026-05-06T10:00:00.000Z"
}
```

**Ejemplo:**
```bash
curl -X POST http://localhost:3000/api/categories \
  -H "Content-Type: application/json" \
  -d '{"name": "Jardinería"}'
```

---

### Update Category
Actualiza una categoría existente.

**Endpoint:** `PUT /categories/:id`

**Parámetros:**
| Nombre | Tipo | Descripción |
|--------|------|-------------|
| id | number | ID de la categoría |

**Body:**
```json
{
  "name": "string (requerido)"
}
```

**Respuesta Exitosa (200):**
```json
{
  "id": 1,
  "name": "Electricidad Actualizada",
  "createdAt": "2026-01-01T00:00:00.000Z"
}
```

**Ejemplo:**
```bash
curl -X PUT http://localhost:3000/api/categories/1 \
  -H "Content-Type: application/json" \
  -d '{"name": "Electricidad y Automatización"}'
```

---

### Delete Category
Elimina una categoría.

**Endpoint:** `DELETE /categories/:id`

**Parámetros:**
| Nombre | Tipo | Descripción |
|--------|------|-------------|
| id | number | ID de la categoría |

**Respuesta Exitosa (200):**
```json
{
  "message": "Category deleted successfully"
}
```

**Ejemplo:**
```bash
curl -X DELETE http://localhost:3000/api/categories/4
```

---

## Requests

### Get All Requests
Obtiene todas las solicitudes de servicio.

**Endpoint:** `GET /requests`

**Respuesta Exitosa (200):**
```json
[
  {
    "id": 1,
    "clientUserId": 1,
    "categoryId": 1,
    "status": "Pendiente",
    "title": "Instalación de interruptores",
    "description": "Necesito instalar 5 interruptores en mi apartamento",
    "location": "Bogotá, Chapinero",
    "budgetCop": 150000,
    "requiredDateMillis": 1746566400000,
    "imageUrl": "https://example.com/image.jpg",
    "isUrgent": false,
    "isActive": true,
    "applicationCount": 3,
    "createdAt": "2026-05-01T10:00:00.000Z"
  }
]
```

**Ejemplo:**
```bash
curl -X GET http://localhost:3000/api/requests
```

---

### Get Request by ID
Obtiene una solicitud específica.

**Endpoint:** `GET /requests/:id`

**Parámetros:**
| Nombre | Tipo | Descripción |
|--------|------|-------------|
| id | number | ID de la solicitud |

**Respuesta Exitosa (200):**
```json
{
  "id": 1,
  "clientUserId": 1,
  "categoryId": 1,
  "status": "Pendiente",
  "title": "Instalación de interruptores",
  "description": "Necesito instalar 5 interruptores en mi apartamento",
  "location": "Bogotá, Chapinero",
  "budgetCop": 150000,
  "requiredDateMillis": 1746566400000,
  "imageUrl": "https://example.com/image.jpg",
  "isUrgent": false,
  "isActive": true,
  "applicationCount": 3,
  "client": {
    "id": 1,
    "name": "Carlos López",
    "photoUrl": "https://example.com/carlos.jpg"
  },
  "category": {
    "id": 1,
    "name": "Electricidad"
  }
}
```

**Ejemplo:**
```bash
curl -X GET http://localhost:3000/api/requests/1
```

---

### Get Requests by Client
Obtiene todas las solicitudes de un cliente específico.

**Endpoint:** `GET /requests/client/:clientId`

**Parámetros:**
| Nombre | Tipo | Descripción |
|--------|------|-------------|
| clientId | number | ID del cliente |

**Respuesta Exitosa (200):**
```json
[
  {
    "id": 1,
    "title": "Instalación de interruptores",
    "status": "Pendiente",
    "createdAt": "2026-05-01T10:00:00.000Z"
  },
  {
    "id": 2,
    "title": "Reparación de tubería",
    "status": "En curso",
    "createdAt": "2026-04-15T14:00:00.000Z"
  }
]
```

**Ejemplo:**
```bash
curl -X GET http://localhost:3000/api/requests/client/1
```

---

### Create Request
Crea una nueva solicitud de servicio.

**Endpoint:** `POST /requests`

**Body:**
```json
{
  "clientUserId": "number (requerido)",
  "categoryId": "number (opcional)",
  "status": "string (opcional, default: 'Borrador')",
  "title": "string (requerido)",
  "description": "string (opcional)",
  "location": "string (opcional)",
  "budgetCop": "number (opcional)",
  "requiredDateMillis": "number (opcional, timestamp en millis)",
  "imageUrl": "string (opcional)",
  "isUrgent": "boolean (opcional, default: false)",
  "isActive": "boolean (opcional, default: true)"
}
```

**Respuesta Exitosa (201):**
```json
{
  "id": 5,
  "clientUserId": 1,
  "categoryId": 1,
  "status": "Borrador",
  "title": "Instalación de lamparas LED",
  "description": "Quiero instalar 10 lamparas LED en mi oficina",
  "location": "Bogotá, Usaquén",
  "budgetCop": 200000,
  "isUrgent": false,
  "isActive": true,
  "applicationCount": 0,
  "createdAt": "2026-05-06T12:00:00.000Z"
}
```

**Ejemplo:**
```bash
curl -X POST http://localhost:3000/api/requests \
  -H "Content-Type: application/json" \
  -d '{
    "clientUserId": 1,
    "categoryId": 1,
    "title": "Instalación de lamparas LED",
    "description": "Quiero instalar 10 lamparas LED en mi oficina",
    "location": "Bogotá, Usaquén",
    "budgetCop": 200000,
    "isUrgent": false
  }'
```

---

### Update Request
Actualiza una solicitud existente.

**Endpoint:** `PUT /requests/:id`

**Parámetros:**
| Nombre | Tipo | Descripción |
|--------|------|-------------|
| id | number | ID de la solicitud |

**Body (parcial):**
```json
{
  "status": "string (opcional: 'Borrador', 'Pendiente', 'En curso', 'Cerrado')",
  "title": "string (opcional)",
  "description": "string (opcional)",
  "location": "string (opcional)",
  "budgetCop": "number (opcional)",
  "isUrgent": "boolean (opcional)",
  "isActive": "boolean (opcional)"
}
```

**Respuesta Exitosa (200):**
```json
{
  "id": 1,
  "status": "Pendiente",
  "updatedAt": "2026-05-06T14:00:00.000Z"
}
```

**Ejemplo:**
```bash
curl -X PUT http://localhost:3000/api/requests/1 \
  -H "Content-Type: application/json" \
  -d '{
    "status": "Pendiente",
    "isUrgent": true
  }'
```

---

### Delete Request
Elimina una solicitud.

**Endpoint:** `DELETE /requests/:id`

**Parámetros:**
| Nombre | Tipo | Descripción |
|--------|------|-------------|
| id | number | ID de la solicitud |

**Respuesta Exitosa (200):**
```json
{
  "message": "Request deleted successfully"
}
```

**Ejemplo:**
```bash
curl -X DELETE http://localhost:3000/api/requests/1
```

---

## Services

### Get All Services
Obtiene todos los servicios confirmados.

**Endpoint:** `GET /services`

**Respuesta Exitosa (200):**
```json
[
  {
    "id": 1,
    "requestId": 1,
    "clientUserId": 1,
    "providerUserId": 2,
    "title": "Instalación de interruptores",
    "summary": "Se instalaron 5 interruptores nuevos",
    "location": "Bogotá, Chapinero",
    "totalPriceCop": 120000,
    "scheduledAt": "2026-05-10T09:00:00.000Z",
    "startedAt": "2026-05-10T09:15:00.000Z",
    "completedAt": "2026-05-10T11:30:00.000Z",
    "status": "COMPLETED",
    "createdAt": "2026-05-05T10:00:00.000Z"
  }
]
```

**Ejemplo:**
```bash
curl -X GET http://localhost:3000/api/services
```

---

### Get Service by ID
Obtiene un servicio específico.

**Endpoint:** `GET /services/:id`

**Parámetros:**
| Nombre | Tipo | Descripción |
|--------|------|-------------|
| id | number | ID del servicio |

**Respuesta Exitosa (200):**
```json
{
  "id": 1,
  "requestId": 1,
  "clientUserId": 1,
  "providerUserId": 2,
  "title": "Instalación de interruptores",
  "summary": "Se instalaron 5 interruptores nuevos",
  "location": "Bogotá, Chapinero",
  "totalPriceCop": 120000,
  "scheduledAt": "2026-05-10T09:00:00.000Z",
  "startedAt": "2026-05-10T09:15:00.000Z",
  "completedAt": "2026-05-10T11:30:00.000Z",
  "status": "COMPLETED",
  "client": {
    "id": 1,
    "name": "Carlos López",
    "photoUrl": "https://example.com/carlos.jpg"
  },
  "provider": {
    "id": 2,
    "name": "Juan Pérez",
    "photoUrl": "https://example.com/juan.jpg",
    "rating": 4.8
  }
}
```

**Ejemplo:**
```bash
curl -X GET http://localhost:3000/api/services/1
```

---

### Get Services by Client
Obtiene todos los servicios donde un usuario es cliente.

**Endpoint:** `GET /services/client/:clientId`

**Parámetros:**
| Nombre | Tipo | Descripción |
|--------|------|-------------|
| clientId | number | ID del usuario cliente |

**Respuesta Exitosa (200):**
```json
[
  {
    "id": 1,
    "title": "Instalación de interruptores",
    "status": "COMPLETED",
    "totalPriceCop": 120000,
    "createdAt": "2026-05-05T10:00:00.000Z"
  }
]
```

**Ejemplo:**
```bash
curl -X GET http://localhost:3000/api/services/client/1
```

---

### Get Services by Provider
Obtiene todos los servicios donde un usuario es proveedor.

**Endpoint:** `GET /services/provider/:providerId`

**Parámetros:**
| Nombre | Tipo | Descripción |
|--------|------|-------------|
| providerId | number | ID del usuario proveedor |

**Respuesta Exitosa (200):**
```json
[
  {
    "id": 2,
    "title": "Reparación de tubería",
    "status": "IN_PROGRESS",
    "totalPriceCop": 80000,
    "createdAt": "2026-05-04T08:00:00.000Z"
  }
]
```

**Ejemplo:**
```bash
curl -X GET http://localhost:3000/api/services/provider/2
```

---

### Create Service
Crea un nuevo servicio confirmado.

**Endpoint:** `POST /services`

**Body:**
```json
{
  "requestId": "number (opcional)",
  "clientUserId": "number (requerido)",
  "providerUserId": "number (requerido)",
  "title": "string (requerido)",
  "summary": "string (opcional)",
  "location": "string (opcional)",
  "totalPriceCop": "number (requerido)",
  "scheduledAt": "string (opcional, ISO date)",
  "startedAt": "string (opcional, ISO date)",
  "completedAt": "string (opcional, ISO date)",
  "status": "string (opcional, default: 'SCHEDULED')"
}
```

**Valores de status:**
- `SCHEDULED` - Programado
- `IN_PROGRESS` - En curso
- `COMPLETED` - Completado
- `CANCELLED` - Cancelado

**Respuesta Exitosa (201):**
```json
{
  "id": 3,
  "requestId": 2,
  "clientUserId": 1,
  "providerUserId": 3,
  "title": "Pintura de habitación",
  "summary": "Pintura de 3 habitaciones",
  "location": "Bogotá, Suba",
  "totalPriceCop": 350000,
  "scheduledAt": "2026-05-15T08:00:00.000Z",
  "status": "SCHEDULED",
  "createdAt": "2026-05-06T10:00:00.000Z"
}
```

**Ejemplo:**
```bash
curl -X POST http://localhost:3000/api/services \
  -H "Content-Type: application/json" \
  -d '{
    "requestId": 2,
    "clientUserId": 1,
    "providerUserId": 3,
    "title": "Pintura de habitación",
    "summary": "Pintura de 3 habitaciones con pintura premium",
    "location": "Bogotá, Suba",
    "totalPriceCop": 350000,
    "scheduledAt": "2026-05-15T08:00:00.000Z",
    "status": "SCHEDULED"
  }'
```

---

### Update Service
Actualiza un servicio existente.

**Endpoint:** `PUT /services/:id`

**Parámetros:**
| Nombre | Tipo | Descripción |
|--------|------|-------------|
| id | number | ID del servicio |

**Body (parcial):**
```json
{
  "title": "string (opcional)",
  "summary": "string (opcional)",
  "location": "string (opcional)",
  "totalPriceCop": "number (opcional)",
  "scheduledAt": "string (opcional)",
  "startedAt": "string (opcional)",
  "completedAt": "string (opcional)",
  "status": "string (opcional)"
}
```

**Respuesta Exitosa (200):**
```json
{
  "id": 1,
  "status": "IN_PROGRESS",
  "startedAt": "2026-05-10T09:15:00.000Z",
  "updatedAt": "2026-05-10T09:15:00.000Z"
}
```

**Ejemplo:**
```bash
curl -X PUT http://localhost:3000/api/services/1 \
  -H "Content-Type: application/json" \
  -d '{
    "status": "IN_PROGRESS",
    "startedAt": "2026-05-10T09:15:00.000Z"
  }'
```

---

### Delete Service
Elimina un servicio.

**Endpoint:** `DELETE /services/:id`

**Parámetros:**
| Nombre | Tipo | Descripción |
|--------|------|-------------|
| id | number | ID del servicio |

**Respuesta Exitosa (200):**
```json
{
  "message": "Service deleted successfully"
}
```

**Ejemplo:**
```bash
curl -X DELETE http://localhost:3000/api/services/1
```

---

## Service Applications

### Get All Applications
Obtiene todas las postulaciones a solicitudes de servicio.

**Endpoint:** `GET /applications`

**Respuesta Exitosa (200):**
```json
[
  {
    "id": 1,
    "requestId": 1,
    "providerUserId": 2,
    "presentationMessage": "Hola, tengo experiencia en instalaciones eléctricas.",
    "proposedPriceCop": 130000,
    "evidenceUri": "https://example.com/work-sample.jpg",
    "immediateAvailability": true,
    "status": "PENDING",
    "createdAt": "2026-05-02T10:00:00.000Z"
  }
]
```

**Ejemplo:**
```bash
curl -X GET http://localhost:3000/api/applications
```

---

### Get Application by ID
Obtiene una postulación específica.

**Endpoint:** `GET /applications/:id`

**Parámetros:**
| Nombre | Tipo | Descripción |
|--------|------|-------------|
| id | number | ID de la postulación |

**Respuesta Exitosa (200):**
```json
{
  "id": 1,
  "requestId": 1,
  "providerUserId": 2,
  "presentationMessage": "Hola, tengo experiencia en instalaciones eléctricas.",
  "proposedPriceCop": 130000,
  "evidenceUri": "https://example.com/work-sample.jpg",
  "immediateAvailability": true,
  "status": "PENDING",
  "provider": {
    "id": 2,
    "name": "Juan Pérez",
    "rating": 4.8,
    "photoUrl": "https://example.com/juan.jpg"
  }
}
```

**Ejemplo:**
```bash
curl -X GET http://localhost:3000/api/applications/1
```

---

### Get Applications by Request
Obtiene todas las postulaciones a una solicitud específica.

**Endpoint:** `GET /applications/request/:requestId`

**Parámetros:**
| Nombre | Tipo | Descripción |
|--------|------|-------------|
| requestId | number | ID de la solicitud |

**Respuesta Exitosa (200):**
```json
[
  {
    "id": 1,
    "providerUserId": 2,
    "presentationMessage": "Tengo 10 años de experiencia",
    "proposedPriceCop": 130000,
    "status": "PENDING",
    "createdAt": "2026-05-02T10:00:00.000Z"
  },
  {
    "id": 2,
    "providerUserId": 3,
    "presentationMessage": "Especializado en instalaciones residenciales",
    "proposedPriceCop": 150000,
    "status": "ACCEPTED",
    "createdAt": "2026-05-02T12:00:00.000Z"
  }
]
```

**Ejemplo:**
```bash
curl -X GET http://localhost:3000/api/applications/request/1
```

---

### Get Applications by Provider
Obtiene todas las postulaciones de un proveedor específico.

**Endpoint:** `GET /applications/provider/:providerId`

**Parámetros:**
| Nombre | Tipo | Descripción |
|--------|------|-------------|
| providerId | number | ID del proveedor |

**Respuesta Exitosa (200):**
```json
[
  {
    "id": 1,
    "requestId": 1,
    "presentationMessage": "Tengo experiencia en instalaciones eléctricas",
    "proposedPriceCop": 130000,
    "status": "PENDING",
    "createdAt": "2026-05-02T10:00:00.000Z"
  },
  {
    "id": 3,
    "requestId": 3,
    "presentationMessage": "Disponible de inmediato",
    "proposedPriceCop": 90000,
    "status": "REJECTED",
    "createdAt": "2026-05-03T08:00:00.000Z"
  }
]
```

**Ejemplo:**
```bash
curl -X GET http://localhost:3000/api/applications/provider/2
```

---

### Create Application
Crea una nueva postulación a una solicitud de servicio.

**Endpoint:** `POST /applications`

**Body:**
```json
{
  "requestId": "number (requerido)",
  "providerUserId": "number (requerido)",
  "presentationMessage": "string (requerido)",
  "proposedPriceCop": "number (opcional)",
  "evidenceUri": "string (opcional)",
  "immediateAvailability": "boolean (opcional, default: false)",
  "status": "string (opcional, default: 'PENDING')"
}
```

**Valores de status:**
- `PENDING` - Pendiente
- `ACCEPTED` - Aceptada
- `REJECTED` - Rechazada

**Respuesta Exitosa (201):**
```json
{
  "id": 4,
  "requestId": 1,
  "providerUserId": 4,
  "presentationMessage": "Soy electricista certificado con 15 años de experiencia",
  "proposedPriceCop": 140000,
  "evidenceUri": "https://example.com/certificado.jpg",
  "immediateAvailability": true,
  "status": "PENDING",
  "createdAt": "2026-05-06T11:00:00.000Z"
}
```

**Ejemplo:**
```bash
curl -X POST http://localhost:3000/api/applications \
  -H "Content-Type: application/json" \
  -d '{
    "requestId": 1,
    "providerUserId": 4,
    "presentationMessage": "Soy electricista certificado con 15 años de experiencia",
    "proposedPriceCop": 140000,
    "evidenceUri": "https://example.com/certificado.jpg",
    "immediateAvailability": true
  }'
```

---

### Update Application
Actualiza una postulación existente.

**Endpoint:** `PUT /applications/:id`

**Parámetros:**
| Nombre | Tipo | Descripción |
|--------|------|-------------|
| id | number | ID de la postulación |

**Body (parcial):**
```json
{
  "presentationMessage": "string (opcional)",
  "proposedPriceCop": "number (opcional)",
  "evidenceUri": "string (opcional)",
  "immediateAvailability": "boolean (opcional)",
  "status": "string (opcional)"
}
```

**Respuesta Exitosa (200):**
```json
{
  "id": 1,
  "status": "ACCEPTED",
  "updatedAt": "2026-05-06T14:00:00.000Z"
}
```

**Ejemplo:**
```bash
curl -X PUT http://localhost:3000/api/applications/1 \
  -H "Content-Type: application/json" \
  -d '{
    "status": "ACCEPTED"
  }'
```

---

### Delete Application
Elimina una postulación.

**Endpoint:** `DELETE /applications/:id`

**Parámetros:**
| Nombre | Tipo | Descripción |
|--------|------|-------------|
| id | number | ID de la postulación |

**Respuesta Exitosa (200):**
```json
{
  "message": "Application deleted successfully"
}
```

**Ejemplo:**
```bash
curl -X DELETE http://localhost:3000/api/applications/1
```

---

## Reviews

### Get All Reviews
Obtiene todas las reseñas.

**Endpoint:** `GET /reviews`

**Respuesta Exitosa (200):**
```json
[
  {
    "id": 1,
    "serviceId": 1,
    "reviewerUserId": 1,
    "reviewedUserId": 2,
    "rating": 5,
    "comment": "Excelente trabajo, muy profesional",
    "evidenceImageUrl": "https://example.com/work-photo.jpg",
    "createdAt": "2026-05-10T12:00:00.000Z"
  }
]
```

**Ejemplo:**
```bash
curl -X GET http://localhost:3000/api/reviews
```

---

### Get Review by ID
Obtiene una reseña específica.

**Endpoint:** `GET /reviews/:id`

**Parámetros:**
| Nombre | Tipo | Descripción |
|--------|------|-------------|
| id | number | ID de la reseña |

**Respuesta Exitosa (200):**
```json
{
  "id": 1,
  "serviceId": 1,
  "reviewerUserId": 1,
  "reviewedUserId": 2,
  "rating": 5,
  "comment": "Excelente trabajo, muy profesional",
  "evidenceImageUrl": "https://example.com/work-photo.jpg",
  "service": {
    "id": 1,
    "title": "Instalación de interruptores"
  },
  "reviewer": {
    "id": 1,
    "name": "Carlos López",
    "photoUrl": "https://example.com/carlos.jpg"
  },
  "reviewed": {
    "id": 2,
    "name": "Juan Pérez",
    "photoUrl": "https://example.com/juan.jpg"
  }
}
```

**Ejemplo:**
```bash
curl -X GET http://localhost:3000/api/reviews/1
```

---

### Get Reviews by Service
Obtiene todas las reseñas de un servicio específico.

**Endpoint:** `GET /reviews/service/:serviceId`

**Parámetros:**
| Nombre | Tipo | Descripción |
|--------|------|-------------|
| serviceId | number | ID del servicio |

**Respuesta Exitosa (200):**
```json
[
  {
    "id": 1,
    "reviewerUserId": 1,
    "rating": 5,
    "comment": "Excelente trabajo",
    "createdAt": "2026-05-10T12:00:00.000Z"
  },
  {
    "id": 2,
    "reviewerUserId": 1,
    "rating": 4,
    "comment": "Muy bueno, llegó a tiempo",
    "createdAt": "2026-05-10T12:30:00.000Z"
  }
]
```

**Ejemplo:**
```bash
curl -X GET http://localhost:3000/api/reviews/service/1
```

---

### Create Review
Crea una nueva reseña para un servicio.

**Endpoint:** `POST /reviews`

**Body:**
```json
{
  "serviceId": "number (requerido)",
  "reviewerUserId": "number (requerido)",
  "reviewedUserId": "number (requerido)",
  "rating": "number (requerido, 1-5)",
  "comment": "string (opcional)",
  "evidenceImageUrl": "string (opcional)"
}
```

**Respuesta Exitosa (201):**
```json
{
  "id": 3,
  "serviceId": 1,
  "reviewerUserId": 1,
  "reviewedUserId": 2,
  "rating": 5,
  "comment": "El mejor electricista que he contratado",
  "evidenceImageUrl": "https://example.com/my-work-photo.jpg",
  "createdAt": "2026-05-10T14:00:00.000Z"
}
```

**Ejemplo:**
```bash
curl -X POST http://localhost:3000/api/reviews \
  -H "Content-Type: application/json" \
  -d '{
    "serviceId": 1,
    "reviewerUserId": 1,
    "reviewedUserId": 2,
    "rating": 5,
    "comment": "El mejor electricista que he contratado",
    "evidenceImageUrl": "https://example.com/my-work-photo.jpg"
  }'
```

---

### Update Review
Actualiza una reseña existente.

**Endpoint:** `PUT /reviews/:id`

**Parámetros:**
| Nombre | Tipo | Descripción |
|--------|------|-------------|
| id | number | ID de la reseña |

**Body (parcial):**
```json
{
  "rating": "number (opcional)",
  "comment": "string (opcional)",
  "evidenceImageUrl": "string (opcional)"
}
```

**Respuesta Exitosa (200):**
```json
{
  "id": 1,
  "rating": 4,
  "comment": "Actualicé mi comentario",
  "updatedAt": "2026-05-10T16:00:00.000Z"
}
```

**Ejemplo:**
```bash
curl -X PUT http://localhost:3000/api/reviews/1 \
  -H "Content-Type: application/json" \
  -d '{
    "rating": 4,
    "comment": "Muy buen trabajo, pero llegó 30 minutos tarde"
  }'
```

---

### Delete Review
Elimina una reseña.

**Endpoint:** `DELETE /reviews/:id`

**Parámetros:**
| Nombre | Tipo | Descripción |
|--------|------|-------------|
| id | number | ID de la reseña |

**Respuesta Exitosa (200):**
```json
{
  "message": "Review deleted successfully"
}
```

**Ejemplo:**
```bash
curl -X DELETE http://localhost:3000/api/reviews/1
```

---

## Chat

### Get All Conversations
Obtiene todas las conversaciones.

**Endpoint:** `GET /chat/conversations`

**Respuesta Exitosa (200):**
```json
[
  {
    "id": 1,
    "participantAUserId": 1,
    "participantBUserId": 2,
    "requestId": 1,
    "serviceId": null,
    "lastMessagePreview": "Cuándo puedes venir?",
    "lastMessageAt": "2026-05-06T11:30:00.000Z",
    "createdAt": "2026-05-05T10:00:00.000Z"
  }
]
```

**Ejemplo:**
```bash
curl -X GET http://localhost:3000/api/chat/conversations
```

---

### Get Conversation by ID
Obtiene una conversación específica con sus participantes.

**Endpoint:** `GET /chat/conversations/:id`

**Parámetros:**
| Nombre | Tipo | Descripción |
|--------|------|-------------|
| id | number | ID de la conversación |

**Respuesta Exitosa (200):**
```json
{
  "id": 1,
  "participantAUserId": 1,
  "participantBUserId": 2,
  "requestId": 1,
  "lastMessagePreview": "Cuándo puedes venir?",
  "lastMessageAt": "2026-05-06T11:30:00.000Z",
  "participantA": {
    "id": 1,
    "name": "Carlos López",
    "photoUrl": "https://example.com/carlos.jpg"
  },
  "participantB": {
    "id": 2,
    "name": "Juan Pérez",
    "photoUrl": "https://example.com/juan.jpg"
  }
}
```

**Ejemplo:**
```bash
curl -X GET http://localhost:3000/api/chat/conversations/1
```

---

### Get Conversations by User
Obtiene todas las conversaciones de un usuario específico.

**Endpoint:** `GET /chat/conversations/user/:userId`

**Parámetros:**
| Nombre | Tipo | Descripción |
|--------|------|-------------|
| userId | number | ID del usuario |

**Respuesta Exitosa (200):**
```json
[
  {
    "id": 1,
    "participantAUserId": 1,
    "participantBUserId": 2,
    "lastMessagePreview": "Cuándo puedes venir?",
    "lastMessageAt": "2026-05-06T11:30:00.000Z"
  },
  {
    "id": 2,
    "participantAUserId": 1,
    "participantBUserId": 3,
    "lastMessagePreview": "Perfecto, nos vemos mañana",
    "lastMessageAt": "2026-05-05T18:00:00.000Z"
  }
]
```

**Ejemplo:**
```bash
curl -X GET http://localhost:3000/api/chat/conversations/user/1
```

---

### Create Conversation
Crea una nueva conversación.

**Endpoint:** `POST /chat/conversations`

**Body:**
```json
{
  "participantAUserId": "number (requerido)",
  "participantBUserId": "number (requerido)",
  "requestId": "number (opcional)",
  "serviceId": "number (opcional)",
  "lastMessagePreview": "string (opcional)",
  "lastMessageAt": "string (opcional, ISO date)"
}
```

**Respuesta Exitosa (201):**
```json
{
  "id": 5,
  "participantAUserId": 1,
  "participantBUserId": 4,
  "requestId": 2,
  "lastMessagePreview": "Hola, me interesa tu servicio",
  "lastMessageAt": "2026-05-06T12:00:00.000Z",
  "createdAt": "2026-05-06T12:00:00.000Z"
}
```

**Ejemplo:**
```bash
curl -X POST http://localhost:3000/api/chat/conversations \
  -H "Content-Type: application/json" \
  -d '{
    "participantAUserId": 1,
    "participantBUserId": 4,
    "requestId": 2,
    "lastMessagePreview": "Hola, me interesa tu servicio"
  }'
```

---

### Get Messages by Conversation
Obtiene todos los mensajes de una conversación.

**Endpoint:** `GET /chat/messages/:conversationId`

**Parámetros:**
| Nombre | Tipo | Descripción |
|--------|------|-------------|
| conversationId | number | ID de la conversación |

**Respuesta Exitosa (200):**
```json
[
  {
    "id": 1,
    "conversationId": 1,
    "senderUserId": 1,
    "body": "Hola, tienes disponibilidad para mañana?",
    "attachmentUri": null,
    "isRead": true,
    "sentAt": "2026-05-06T10:00:00.000Z"
  },
  {
    "id": 2,
    "conversationId": 1,
    "senderUserId": 2,
    "body": "Sí, a qué hora te viene bien?",
    "attachmentUri": null,
    "isRead": false,
    "sentAt": "2026-05-06T10:15:00.000Z"
  },
  {
    "id": 3,
    "conversationId": 1,
    "senderUserId": 1,
    "body": "Cuándo puedes venir?",
    "attachmentUri": "https://example.com/foto-trabajo.jpg",
    "isRead": false,
    "sentAt": "2026-05-06T11:30:00.000Z"
  }
]
```

**Ejemplo:**
```bash
curl -X GET http://localhost:3000/api/chat/messages/1
```

---

### Create Message
Envía un nuevo mensaje en una conversación.

**Endpoint:** `POST /chat/messages`

**Body:**
```json
{
  "conversationId": "number (requerido)",
  "senderUserId": "number (requerido)",
  "body": "string (requerido)",
  "attachmentUri": "string (opcional)",
  "isRead": "boolean (opcional, default: false)"
}
```

**Respuesta Exitosa (201):**
```json
{
  "id": 4,
  "conversationId": 1,
  "senderUserId": 2,
  "body": "A las 9am está bien?",
  "attachmentUri": null,
  "isRead": false,
  "sentAt": "2026-05-06T12:00:00.000Z"
}
```

**Ejemplo:**
```bash
curl -X POST http://localhost:3000/api/chat/messages \
  -H "Content-Type: application/json" \
  -d '{
    "conversationId": 1,
    "senderUserId": 2,
    "body": "A las 9am está bien?"
  }'
```

---

### Mark Message as Read
Marca un mensaje como leído.

**Endpoint:** `PUT /chat/messages/:id/read`

**Parámetros:**
| Nombre | Tipo | Descripción |
|--------|------|-------------|
| id | number | ID del mensaje |

**Respuesta Exitosa (200):**
```json
{
  "id": 2,
  "isRead": true,
  "updatedAt": "2026-05-06T12:05:00.000Z"
}
```

**Ejemplo:**
```bash
curl -X PUT http://localhost:3000/api/chat/messages/2/read
```

---

## Notifications

### Get All Notifications
Obtiene todas las notificaciones.

**Endpoint:** `GET /notifications`

**Respuesta Exitosa (200):**
```json
[
  {
    "id": 1,
    "userId": 1,
    "title": "Nueva postulación",
    "description": "Juan Pérez ha postulado a tu solicitud",
    "type": "APPLICATION",
    "isUnread": true,
    "requestId": 1,
    "createdAt": "2026-05-06T10:00:00.000Z"
  }
]
```

**Ejemplo:**
```bash
curl -X GET http://localhost:3000/api/notifications
```

---

### Get Notification by ID
Obtiene una notificación específica.

**Endpoint:** `GET /notifications/:id`

**Parámetros:**
| Nombre | Tipo | Descripción |
|--------|------|-------------|
| id | number | ID de la notificación |

**Respuesta Exitosa (200):**
```json
{
  "id": 1,
  "userId": 1,
  "title": "Nueva postulación",
  "description": "Juan Pérez ha postulado a tu solicitud de instalación de interruptores",
  "type": "APPLICATION",
  "isUnread": true,
  "requestId": 1,
  "applicationId": 1,
  "createdAt": "2026-05-06T10:00:00.000Z"
}
```

**Ejemplo:**
```bash
curl -X GET http://localhost:3000/api/notifications/1
```

---

### Get Notifications by User
Obtiene todas las notificaciones de un usuario.

**Endpoint:** `GET /notifications/user/:userId`

**Parámetros:**
| Nombre | Tipo | Descripción |
|--------|------|-------------|
| userId | number | ID del usuario |

**Respuesta Exitosa (200):**
```json
[
  {
    "id": 1,
    "title": "Nueva postulación",
    "description": "Juan Pérez ha postulado a tu solicitud",
    "type": "APPLICATION",
    "isUnread": true,
    "requestId": 1,
    "createdAt": "2026-05-06T10:00:00.000Z"
  },
  {
    "id": 2,
    "title": "Nuevo mensaje",
    "description": "Tienes un mensaje de Carlos López",
    "type": "MESSAGE",
    "isUnread": false,
    "conversationId": 1,
    "createdAt": "2026-05-05T15:00:00.000Z"
  }
]
```

**Ejemplo:**
```bash
curl -X GET http://localhost:3000/api/notifications/user/1
```

---

### Get Unread Notifications by User
Obtiene solo las notificaciones no leídas de un usuario.

**Endpoint:** `GET /notifications/user/:userId/unread`

**Parámetros:**
| Nombre | Tipo | Descripción |
|--------|------|-------------|
| userId | number | ID del usuario |

**Respuesta Exitosa (200):**
```json
[
  {
    "id": 1,
    "title": "Nueva postulación",
    "description": "Juan Pérez ha postulado a tu solicitud",
    "type": "APPLICATION",
    "isUnread": true,
    "requestId": 1,
    "createdAt": "2026-05-06T10:00:00.000Z"
  }
]
```

**Ejemplo:**
```bash
curl -X GET http://localhost:3000/api/notifications/user/1/unread
```

---

### Create Notification
Crea una nueva notificación.

**Endpoint:** `POST /notifications`

**Body:**
```json
{
  "userId": "number (requerido)",
  "title": "string (requerido)",
  "description": "string (requerido)",
  "type": "string (requerido)",
  "isUnread": "boolean (opcional, default: true)",
  "requestId": "number (opcional)",
  "serviceId": "number (opcional)",
  "conversationId": "number (opcional)",
  "applicationId": "number (opcional)"
}
```

**Valores de type:**
- `APPLICATION` - Nueva postulación
- `MESSAGE` - Nuevo mensaje
- `CONFIRMED` - Servicio confirmado
- `PAYMENT` - Pago recibido
- `REMINDER` - Recordatorio

**Respuesta Exitosa (201):**
```json
{
  "id": 5,
  "userId": 1,
  "title": "Servicio confirmado",
  "description": "Tu servicio ha sido confirmado para mañana a las 9am",
  "type": "CONFIRMED",
  "isUnread": true,
  "serviceId": 1,
  "createdAt": "2026-05-06T12:00:00.000Z"
}
```

**Ejemplo:**
```bash
curl -X POST http://localhost:3000/api/notifications \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "title": "Servicio confirmado",
    "description": "Tu servicio ha sido confirmado para mañana a las 9am",
    "type": "CONFIRMED",
    "serviceId": 1
  }'
```

---

### Mark Notification as Read
Marca una notificación como leída.

**Endpoint:** `PUT /notifications/:id/read`

**Parámetros:**
| Nombre | Tipo | Descripción |
|--------|------|-------------|
| id | number | ID de la notificación |

**Respuesta Exitosa (200):**
```json
{
  "id": 1,
  "isUnread": false,
  "updatedAt": "2026-05-06T12:30:00.000Z"
}
```

**Ejemplo:**
```bash
curl -X PUT http://localhost:3000/api/notifications/1/read
```

---

### Mark All Notifications as Read
Marca todas las notificaciones de un usuario como leídas.

**Endpoint:** `PUT /notifications/user/:userId/read-all`

**Parámetros:**
| Nombre | Tipo | Descripción |
|--------|------|-------------|
| userId | number | ID del usuario |

**Respuesta Exitosa (200):**
```json
{
  "message": "All notifications marked as read",
  "count": 5
}
```

**Ejemplo:**
```bash
curl -X PUT http://localhost:3000/api/notifications/user/1/read-all
```

---

### Delete Notification
Elimina una notificación.

**Endpoint:** `DELETE /notifications/:id`

**Parámetros:**
| Nombre | Tipo | Descripción |
|--------|------|-------------|
| id | number | ID de la notificación |

**Respuesta Exitosa (200):**
```json
{
  "message": "Notification deleted successfully"
}
```

**Ejemplo:**
```bash
curl -X DELETE http://localhost:3000/api/notifications/1
```

---

## Códigos de Error Comunes

| Código | Descripción |
|--------|-------------|
| 200 | OK - Solicitud exitosa |
| 201 | Created - Recurso creado exitosamente |
| 400 | Bad Request - Datos inválidos o faltantes |
| 401 | Unauthorized - Token JWT inválido o expirado |
| 403 | Forbidden - No tiene permisos para esta acción |
| 404 | Not Found - Recurso no encontrado |
| 500 | Internal Server Error - Error del servidor |

---

## Autenticación

La API usa autenticación JWT. Para endpoints protegidos, incluye el token en el header:

```
Authorization: Bearer <access_token>
```

---

## Notas

- Todos los timestamps están en formato ISO 8601 (UTC)
- Los precios están en COP (Pesos Colombianos)
- Los IDs de recursos son siempre números enteros
- Los body de POST/PUT aceptan campos parciales para actualizaciones
