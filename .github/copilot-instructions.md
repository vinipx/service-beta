# Service Beta — Copilot Agent Instructions

## Repository Purpose
Secondary Spring Boot 3.x RESTful microservice exposing CRUD APIs for Order management. Depends on `common-library` for shared DTOs, utilities, and exception handling. Communicates with `service-alpha` via REST.

## Architecture
- **Language**: Java 21
- **Framework**: Spring Boot 3.2.x
- **Build Tool**: Maven 3.9+
- **Database**: H2 (dev/test), PostgreSQL (prod)
- **Port**: 8082

## Package Structure
```
com.ecosystem.beta.config       → Spring configuration classes
com.ecosystem.beta.controller   → REST controllers (@RestController)
com.ecosystem.beta.service      → Business logic (@Service)
com.ecosystem.beta.repository   → Data access (@Repository, Spring Data JPA)
com.ecosystem.beta.model        → JPA entity classes (@Entity)
com.ecosystem.beta.client       → REST clients for inter-service communication
com.ecosystem.beta.dto          → Service-specific DTOs
```

## Rules for Agents
1. Follow the layered architecture strictly: Controller → Service → Repository.
2. Controllers MUST only handle HTTP concerns. No business logic.
3. Services MUST use constructor injection.
4. Inter-service calls to service-alpha MUST use RestClient with error handling.
5. Use common-library DTOs where applicable — do NOT create duplicate DTOs.
6. All REST endpoints MUST have integration tests.
7. Exception handling via @ControllerAdvice using common-library exception hierarchy.
8. Minimum 80% test coverage.
