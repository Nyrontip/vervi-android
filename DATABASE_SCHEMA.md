# Esquema de Base de Datos Room - Vervi App

## Resumen Ejecutivo
El esquema Room para Vervi está diseñado alrededor de **4 dominios principales**:
1. **Usuarios & Categorías**: Perfiles de clientes/prestadores con especialidades
2. **Solicitudes & Postulaciones**: Flujo de solicitud de servicios
3. **Servicios & Evidencia**: Servicios completados con registro fotográfico
4. **Comunicación**: Chat y notificaciones entre usuarios

---

## Entidades Room

### 1. **UserEntity** (`users` table)
Representa un usuario (cliente o prestador).
```
Campos clave:
- id (PK, autoincrement)
- email (UNIQUE)
- name, bio, location, photoUrl
- rating, reviewCount
- suggestedPriceCop (para prestadores)
- isProvider, isOnline
- projectCount, requestCount (estadísticas)
- createdAt, updatedAt (timestamps)
```

**Relaciones:**
- 1:N con `RequestEntity` (usuario crea solicitudes)
- N:N con `CategoryEntity` vía `UserCategoryCrossRef`
- 1:N con `ServiceEntity` (como cliente o prestador)
- 1:N con `ReviewEntity` (reseñas que recibe/escribe)
- 1:N con `ConversationEntity` (conversaciones)
- 1:N con `NotificationEntity` (notificaciones)

---

### 2. **CategoryEntity** (`categories` table)
Especialidades/categorías de servicios (Plomería, Carpintería, Electricidad, etc.).
```
Campos clave:
- id (PK, autoincrement)
- name (UNIQUE)
- createdAt
```

**Relaciones:**
- N:N con `UserEntity` vía `UserCategoryCrossRef`
- 1:N con `RequestEntity` (solicitud tiene categoría)

---

### 3. **UserCategoryCrossRef** (tabla puente N:N)
Vincula usuarios con sus categorías de especialización.
```
Campos clave:
- userId (FK → UserEntity.id, PRIMARY KEY)
- categoryId (FK → CategoryEntity.id, PRIMARY KEY)
```

---

### 4. **RequestEntity** (`requests` table)
Una solicitud de servicio abierta (publicada por un cliente).
```
Campos clave:
- id (PK, autoincrement)
- clientUserId (FK → UserEntity.id, nullable)
- categoryId (FK → CategoryEntity.id, nullable)
- status (ENUM: "Borrador", "Pendiente", "En curso", "Cerrado")
- title, description
- location
- budgetCop (presupuesto en pesos)
- requiredDateMillis (fecha requerida)
- imageUrl (foto de la solicitud)
- isUrgent (boolean)
- isActive (tab activas/finalizadas)
- applicationCount (número de postulaciones)
- createdAt, updatedAt, closedAt (timestamps)
- date, applications, buttonText (legacy UI fields)
```

**Relaciones:**
- N:1 con `UserEntity` (cliente)
- N:1 con `CategoryEntity`
- 1:N con `RequestAttachmentEntity` (fotos adjuntas)
- 1:N con `ServiceApplicationEntity` (postulaciones)
- 1:1 con `ServiceEntity` (cuando se acepta)
- 1:N con `NotificationEntity` (notificaciones relacionadas)

---

### 5. **RequestAttachmentEntity** (`request_attachments` table)
Archivos adjuntos (imágenes) a una solicitud (máx 3).
```
Campos clave:
- id (PK, autoincrement)
- requestId (FK → RequestEntity.id, CASCADE)
- uri (ruta o URL de la imagen)
- mimeType (tipo MIME)
- sortOrder (orden de visualización)
- createdAt
```

---

### 6. **ServiceApplicationEntity** (`service_applications` table)
Una postulación de un prestador a una solicitud.
```
Campos clave:
- id (PK, autoincrement)
- requestId (FK → RequestEntity.id, CASCADE)
- providerUserId (FK → UserEntity.id, CASCADE)
- presentationMessage (texto de presentación del prestador)
- proposedPriceCop (precio propuesto)
- evidenceUri (foto de portafolio/evidencia)
- immediateAvailability (boolean)
- status (ENUM: "PENDING", "ACCEPTED", "REJECTED")
- createdAt, updatedAt
```

**Relaciones:**
- N:1 con `RequestEntity`
- N:1 con `UserEntity` (prestador)
- 1:N con `NotificationEntity` (notificaciones de postulación)

---

### 7. **ServiceEntity** (`services` table)
Un servicio completado o en progreso (resultado de una solicitud aceptada).
```
Campos clave:
- id (PK, autoincrement)
- requestId (FK → RequestEntity.id, nullable, SET_NULL)
- clientUserId (FK → UserEntity.id, CASCADE)
- providerUserId (FK → UserEntity.id, CASCADE)
- title, summary, location
- totalPriceCop (precio final)
- scheduledAt, startedAt, completedAt (timestamps de lifecycle)
- status (ENUM: "SCHEDULED", "IN_PROGRESS", "COMPLETED", "CANCELLED")
- createdAt, updatedAt
```

**Relaciones:**
- N:1 con `RequestEntity` (origen)
- N:1 con `UserEntity` (cliente)
- N:1 con `UserEntity` (prestador)
- 1:N con `ServiceEvidenceEntity` (fotos de ejecución)
- 1:N con `ReviewEntity` (reseñas del servicio)
- 1:N con `ConversationEntity` (chats ligados al servicio)
- 1:N con `NotificationEntity` (notificaciones del servicio)

---

### 8. **ServiceEvidenceEntity** (`service_evidence` table)
Fotos de evidencia de un servicio completado (galería 4 imágenes).
```
Campos clave:
- id (PK, autoincrement)
- serviceId (FK → ServiceEntity.id, CASCADE)
- imageUrl (URL de la imagen)
- caption (descripción opcional)
- sortOrder (orden en galería)
- createdAt
```

---

### 9. **ReviewEntity** (`reviews` table)
Reseña/calificación de un servicio (por cliente al prestador, o viceversa).
```
Campos clave:
- id (PK, autoincrement)
- serviceId (FK → ServiceEntity.id, CASCADE)
- reviewerUserId (FK → UserEntity.id, CASCADE)
- reviewedUserId (FK → UserEntity.id, CASCADE)
- rating (1-5)
- comment (texto opcional)
- evidenceImageUrl (foto de evidencia del servicio realizado)
- createdAt
```

---

### 10. **ConversationEntity** (`conversations` table)
Un hilo de chat entre dos usuarios (ligado opcionalmente a solicitud/servicio).
```
Campos clave:
- id (PK, autoincrement)
- participantAUserId (FK → UserEntity.id, CASCADE)
- participantBUserId (FK → UserEntity.id, CASCADE)
- requestId (FK → RequestEntity.id, nullable, SET_NULL)
- serviceId (FK → ServiceEntity.id, nullable, SET_NULL)
- lastMessagePreview (texto del último mensaje para preview)
- lastMessageAt (timestamp del último mensaje)
- createdAt
```

**Relaciones:**
- N:1 con `UserEntity` (participante A)
- N:1 con `UserEntity` (participante B)
- 1:N con `MessageEntity` (mensajes del chat)
- 1:N con `NotificationEntity` (notificaciones de nuevo mensaje)

---

### 11. **MessageEntity** (`messages` table)
Un mensaje en una conversación.
```
Campos clave:
- id (PK, autoincrement)
- conversationId (FK → ConversationEntity.id, CASCADE)
- senderUserId (FK → UserEntity.id, CASCADE)
- body (texto del mensaje)
- attachmentUri (URL de archivo adjunto, opcional)
- isRead (boolean)
- sentAt (timestamp)
```

---

### 12. **NotificationEntity** (`notifications` table)
Una notificación push/in-app para un usuario.
```
Campos clave:
- id (PK, autoincrement)
- userId (FK → UserEntity.id, CASCADE)
- title, description
- type (ENUM: "APPLICATION", "MESSAGE", "CONFIRMED", "PAYMENT", "REMINDER")
- isUnread (boolean)
- requestId, serviceId, conversationId, applicationId (FKs opcionales)
- createdAt
```

---

## Tablas Puente

### UserCategoryCrossRef (N:N)
Vincula usuarios con categorías.
- `(userId, categoryId)` es la clave compuesta primaria
- Índices en ambas columnas para búsquedas rápidas

---

## Índices Principales

Optimizaciones por pantalla/query:
- **Home**: `Service.status`, `Service.clientUserId`, `Service.createdAt`
- **Mis Solicitudes**: `Request.clientUserId`, `Request.isActive`, `Request.status`
- **Historial de Servicios**: `Service.providerUserId`, `Service.status`, `Service.completedAt`
- **Chat**: `Conversation.participantAUserId`, `Conversation.participantBUserId`, `Conversation.lastMessageAt`
- **Notificaciones**: `Notification.userId`, `Notification.isUnread`, `Notification.createdAt`

---

## Relaciones Visuales

```
┌─────────────────────────────────────────────────────────┐
│                    USER (Cliente/Prestador)            │
│  id, name, email, rating, isProvider, categories[]    │
└────────────────────┬────────────────────────────────────┘
                     │ 1:N
         ┌───────────┼───────────┬─────────────┐
         │           │           │             │
    ┌────▼─┐   ┌─────▼──┐  ┌─────▼──┐  ┌────▼─────┐
    │REQUEST│   │SERVICE │  │CHAT    │  │NOTIF     │
    └────┬─┘   └────┬────┘  │        │  │          │
         │ 1:N      │ 1:N    │        │  │          │
         │          │        │        │  │          │
    ┌────▼──────────▼───┐    │        │  │          │
    │ ATTACHMENTS       │    │        │  │          │
    │ APPLICATIONS      │    │        │  │          │
    └───────────────────┘    │        │  │          │
                            │ 1:N    │  │          │
                        ┌───▼──────┐ │  │          │
                        │EVIDENCE  │ │  │          │
                        │REVIEWS   │ │  │          │
                        └──────────┘ │  │          │
                                     │  │          │
                            ┌────────▼──▼──────────▼┐
                            │    MESSAGES           │
                            └───────────────────────┘
```

---

## Transiciones de Estado

### Request
- Borrador → Pendiente (publicar)
- Pendiente → En curso (aceptar postulación)
- En curso → Cerrado (servicio completado/cancelado)

### Service
- SCHEDULED → IN_PROGRESS → COMPLETED → (REVIEW)
- o SCHEDULED → CANCELLED

### ServiceApplication
- PENDING → ACCEPTED o REJECTED

### Message
- Enviado → Entregado → Leído (isRead: false → true)

---

## Notas de Implementación

1. **Timestamps**: Usar `System.currentTimeMillis()` para `createdAt`, `updatedAt`.
2. **Soft Deletes**: Considerar agregar `deletedAt: Long? = null` si se requiere auditoría.
3. **Converters**: Para `Long` (millis), usar `Long` directamente; Room los mapea a INTEGER.
4. **Cascadas**: Configuradas según la lógica de negocio:
   - User → [Request, Service, Reviews, Conversations, Notifications] (CASCADE)
   - Request → [Attachments, Applications] (CASCADE)
   - Service → [Evidence, Reviews, Conversations] (CASCADE)
   - Conversation → [Messages] (CASCADE)
5. **Indices**: Crear sobre FKs y columnas de búsqueda común (status, dates, isUnread).

