# Copilot Agent Instructions — service-beta

## Repository Purpose
`service-beta` is a Spring Boot RESTful microservice responsible for CRUD APIs in the Beta domain.
It depends on `vinipx/common-library` for shared helpers and reusable components.

## Boundaries & Responsibilities
- Owns Beta-specific domain behavior and API endpoints.
- Should not copy shared logic already suitable for `common-library`.
- Contract changes must be analyzed for compatibility and consumer impact.

## Tech Stack
- Java 21+
- Spring Boot 3.x
- Maven
- JUnit 5, Mockito, Testcontainers (integration level)

## Agent Operating Instructions
1. Ingest Jira user story and acceptance criteria first.
2. Perform cross-repository impact check:
   - `service-beta` direct impact
   - `common-library` shared code impact
   - Compatibility with `service-alpha`
3. Implement per `java-coding-guidelines.md`.
4. Include robust tests:
   - Unit tests for business logic
   - Integration tests for controllers/repositories/external behavior
5. Keep API contracts explicit and version-safe.
6. Document behavior changes in PR description.
7. Reference Jira key in branch/commits/PR.

## Definition of Done (Agent)
- No unresolved TODOs
- Green CI
- Coverage maintained or improved
- PR opened with complete implementation + test rationale
