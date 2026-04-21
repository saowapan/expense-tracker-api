# Expense Tracker API

REST API for personal expense tracking with JWT authentication, built with Spring Boot 3.4 and PostgreSQL.

Companion frontend: [expense-tracker](https://github.com/saowapan/expense-tracker)

## What it does

- CRUD operations for **Expenses** (amount, category, payment method, currency, date)
- **Date-range filtering** via query params (`?from=...&to=...`) — powers the dashboard's period filter
- **JWT-based authentication** with BCrypt-hashed passwords
- **Flyway-managed database migrations** (no `ddl-auto=update` surprises)
- **DTOs separate from entities** — API contract decoupled from persistence layer
- **Global exception handling** with consistent JSON error responses
- **Interactive API documentation** via Swagger UI

## Tech Stack

- **Java 21** (records, pattern matching, modern language features)
- **Spring Boot 3.4** (Spring Framework 6, Jakarta EE)
- **Spring Data JPA** with Hibernate
- **Spring Security** (stateless, JWT filter chain)
- **PostgreSQL 16** (production-grade, ACID, easy future scaling)
- **Flyway** for versioned, reproducible schema migrations
- **Lombok** for reduced boilerplate on entities
- **JJWT 0.12** for token signing and verification
- **SpringDoc OpenAPI** for Swagger UI
- **Docker Compose** for reproducible dev environment

## Architecture Highlights

### Layered architecture
Controller → Service → Repository. Classic separation of concerns. Business rules live in the service layer, not leaked into controllers or entities.

### DTOs, not entities, over the wire
`ExpenseRequest` (input) and `ExpenseResponse` (output) are Java records. Entities never leak to the API:
- Protects against accidentally exposing DB fields
- API contract evolves independently of persistence schema
- Validation annotations live on DTOs, not entities — cleaner separation

### Stateless JWT auth
No sessions, no shared session store. Every request verified by signature. Scales horizontally.

- Secret key is HS256-compliant (256-bit minimum), loaded from environment
- Custom `JwtAuthFilter` extends `OncePerRequestFilter`, runs before `UsernamePasswordAuthenticationFilter`
- `UserDetailsService` is interface-driven — moving from in-memory to JPA-backed users is one class to swap

### Flyway for schema, not `ddl-auto=update`
Entity changes don't secretly rewrite the DB. `spring.jpa.hibernate.ddl-auto=validate` means Hibernate **checks** that the schema matches entities and refuses to start if it doesn't. Schema is versioned SQL in `src/main/resources/db/migration/`.

### Global exception handler
`@RestControllerAdvice` catches:
- `ResourceNotFoundException` → 404 with clean JSON
- `MethodArgumentNotValidException` → 400 with field-level errors, sorted
- Catch-all `Exception.class` → 500, no stack traces leaked to response

### Externalized configuration
All secrets (JWT key, DB password, admin credentials) live in `.env`, never committed. `.env.example` shows what vars are needed.

## Running locally

### Prerequisites
- Java 21
- Docker & Docker Compose
- Maven (or use the included `./mvnw` wrapper)

### 1. Clone and configure

```bash
git clone https://github.com/saowapan/expense-tracker-api.git
cd expense-tracker-api
cp .env.example .env
```

Edit `.env` with your own values:

```properties
DB_URL=jdbc:postgresql://localhost:5433/expense_tracker
DB_USERNAME=expense_user
DB_PASSWORD=expense_pass
JWT_SECRET=generate-a-random-string-of-at-least-32-chars
JWT_EXPIRATION_MS=3600000
ADMIN_USERNAME=admin
ADMIN_PASSWORD=change-me-in-your-local-env
```

### 2. Start PostgreSQL

```bash
docker compose up -d
```

This starts PostgreSQL 16 on port **5433** (mapped from container's 5432).

### 3. Run the application

```bash
./mvnw spring-boot:run
```

Flyway will automatically run migrations on startup. The API will be available at `http://localhost:8080`.

### 4. Open Swagger UI

Visit [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) to explore and test the API interactively.

## API Endpoints

### Authentication

| Method | Endpoint          | Description       | Auth required |
|--------|-------------------|-------------------|---------------|
| POST   | `/api/auth/login` | Get a JWT token   | No            |

### Expenses

| Method | Endpoint              | Description                  | Auth required |
|--------|-----------------------|------------------------------|---------------|
| GET    | `/api/expenses`       | List all expenses            | Yes           |
| GET    | `/api/expenses?from=YYYY-MM-DD&to=YYYY-MM-DD` | Filter by date range | Yes |
| GET    | `/api/expenses/{id}`  | Get expense by ID            | Yes           |
| POST   | `/api/expenses`       | Create a new expense         | Yes           |
| PUT    | `/api/expenses/{id}`  | Update an existing expense   | Yes           |
| DELETE | `/api/expenses/{id}`  | Delete an expense            | Yes           |

### Example: Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "your-password"}'
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "expiresIn": 3600000
}
```

### Example: Create expense

```bash
curl -X POST http://localhost:8080/api/expenses \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <your-token>" \
  -d '{
    "amount": 42.50,
    "category": "Food",
    "description": "Lunch",
    "paymentMethod": "Credit Card",
    "currency": "THB",
    "expenseDate": "2026-04-21"
  }'
```

## Project Structure

```
src/main/java/com/saowapan/expense_tracker_api/
├── config/             # Spring configuration (Security, CORS, Password)
├── controller/         # REST controllers (Auth, Expense)
├── service/            # Business logic
├── repository/         # Spring Data JPA interfaces
├── model/              # JPA entities
├── dto/                # Request/Response DTOs (Java records)
├── security/           # JWT service, auth filter, UserDetailsService
└── exception/          # Custom exceptions & global handler
```