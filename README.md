# service-beta

Secondary Spring Boot 3.2.x RESTful microservice exposing CRUD APIs for Order management. Depends on `common-library` for shared DTOs, utilities, and exception handling. Communicates with `service-alpha` via REST for user data validation.

## Prerequisites

- Java 21
- Maven 3.9+
- `common-library` installed locally (see Build Instructions)

## Build Instructions

### 1. Install common-library

```bash
cd common-library
mvn install -DskipTests
cd ..
```

### 2. Build service-beta

```bash
mvn clean verify
```

### 3. Run

```bash
mvn spring-boot:run
```

## API Documentation

| Method | Endpoint | Description | Status Codes |
|--------|----------|-------------|--------------|
| GET | `/api/v1/orders` | List all orders | 200 |
| GET | `/api/v1/orders/{id}` | Get order by ID | 200, 404 |
| POST | `/api/v1/orders` | Create a new order | 201, 400, 404 |
| PUT | `/api/v1/orders/{id}` | Update an existing order | 200, 400, 404 |
| DELETE | `/api/v1/orders/{id}` | Delete an order | 204, 404 |

## Inter-Service Communication

`service-beta` calls `service-alpha` to validate that a user exists before creating an order.

- **service-alpha base URL**: `http://localhost:8081` (configurable via `services.alpha.base-url`)
- **Endpoint called**: `GET /api/v1/users/{id}`
- If the user is not found, a `404 Not Found` is returned to the client.

## Running Locally

1. Start `service-alpha` on port `8081`
2. Start `service-beta`:
   ```bash
   mvn spring-boot:run
   ```
   The service will be available at `http://localhost:8082`

## H2 Console

Access the in-memory H2 database console at:
`http://localhost:8082/h2-console`

JDBC URL: `jdbc:h2:mem:betadb`

## Actuator Endpoints

- Health: `http://localhost:8082/actuator/health`
- Info: `http://localhost:8082/actuator/info`
- Metrics: `http://localhost:8082/actuator/metrics`