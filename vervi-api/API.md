# API.md - Vervi API Documentation

## Base URL
```
http://localhost:3000/api
```

## Authentication
- JWT Bearer Token required for protected endpoints
- Header: `Authorization: Bearer <token>`
- Secret: `JWT_SECRET` (from `.env`)

---

## Endpoints

### 1. Auth

#### POST /auth/register
- Description: Register new user
- Headers: None (public)
- Request Body:
```json
{
  "email": "string",
  "password": "string", 
  "name": "string",
  "bio": "string?",
  "location": "string?",
  "photoUrl": "string?",
  "rating": "number?",
  "reviewCount": "number?",
  "suggestedPriceCop": "number?",
  "isProvider": "boolean?",
  "isOnline": "boolean?",
  "categoryIds": "number[]?"
}
```
- Response: `201 Created` with created user (hashed password)
- Codes: `201`, `400`

#### POST /auth/login
- Description: Login and get JWT token
- Headers: None (public)
- Request Body:
```json
{
  "email": "string",
  "password": "string"
}
```
- Response `200 OK`:
```json
{
  "access_token": "string",
  "user": { ...UserDto }
}
```
- Codes: `200`, `401`

### 2. Users

#### GET /users
- Description: Get all users
- Headers: None (public)

#### GET /users/:id
- Description: Get user by ID
- Path params: `id` (number)

#### GET /users/profile/me
- Description: Get current user profile
- Headers: `Authorization: Bearer <token>` (required)

#### PUT /users/:id
- Description: Update user
- Path params: `id` (number)
- Request Body: Partial<User>

#### DELETE /users/:id
- Description: Delete user
- Path params: `id` (number)

### 3. Categories

#### GET /categories
- Description: Get all categories
- Headers: None (public)

#### GET /categories/:id
- Description: Get category by ID
- Path params: `id` (number)

### 4. Requests

#### GET /requests
- Description: Get all requests
- Headers: None (public)

#### GET /requests/:id
- Description: Get request by ID
- Path params: `id` (number)

#### GET /requests/client/:clientId
- Description: Get requests by client
- Path params: `clientId` (number)

#### POST /requests
- Description: Create request
- Request Body: Partial<Request>

#### PUT /requests/:id
- Description: Update request
- Path params: `id` (number)

#### DELETE /requests/:id
- Description: Delete request
- Path params: `id` (number)

### 5. Services

#### GET /services
- Description: Get all services

#### GET /services/:id
- Description: Get service by ID
- Path params: `id` (number)

#### GET /services/client/:clientId
- Description: Get services by client

#### GET /services/provider/:providerId
- Description: Get services by provider

### 6. Applications

#### GET /applications
- Description: Get all applications

#### GET /applications/request/:requestId
- Description: Get applications by request

### 7. Reviews

#### GET /reviews
- Description: Get all reviews

#### GET /reviews/service/:serviceId
- Description: Get reviews by service

### 8. Chat

#### GET /chat/conversations
- Description: Get all conversations

#### GET /chat/conversations/user/:userId
- Description: Get conversations by user

#### GET /chat/messages/:conversationId
- Description: Get messages by conversation

#### POST /chat/messages
- Description: Create message

### 9. Notifications

#### GET /notifications/user/:userId
- Description: Get notifications by user

#### PUT /notifications/:id/read
- Description: Mark notification as read

---

## Entities

### UserDto
```json
{
  "id": "number",
  "email": "string",
  "name": "string",
  "bio": "string?",
  "location": "string?",
  "photoUrl": "string?",
  "rating": "number",
  "reviewCount": "number",
  "suggestedPriceCop": "number?",
  "isProvider": "boolean",
  "isOnline": "boolean",
  "projectCount": "number",
  "requestCount": "number",
  "categories": "CategoryDto[]",
  "createdAt": "string",
  "updatedAt": "string"
}
```

### CategoryDto
```json
{
  "id": "number",
  "name": "string"
}
```

### RequestDto
```json
{
  "id": "number",
  "clientUserId": "number?",
  "categoryId": "number?",
  "status": "string",
  "title": "string",
  "description": "string?",
  "location": "string?",
  "budgetCop": "number?",
  "requiredDateMillis": "number?",
  "imageUrl": "string?",
  "isUrgent": "boolean",
  "isActive": "boolean",
  "applicationCount": "number",
  "category": "CategoryDto?",
  "client": "UserDto?",
  "createdAt": "string?",
  "updatedAt": "string?"
}
```

---

## Environment Variables

```
DB_HOST=localhost
DB_PORT=5432
DB_USERNAME=postgres
DB_PASSWORD=postgres
DB_DATABASE=vervi_db
NODE_ENV=development
PORT=3000
JWT_SECRET=vervi_super_secret_key_local_dev_2024
```

---

## NOTES

- All endpoints are public (no JWT protection except GET /users/profile/me)
- No pagination implemented
- CORS enabled for all origins
- Passwords stored hashed with bcrypt
- JWT payload contains: sub, email, isProvider