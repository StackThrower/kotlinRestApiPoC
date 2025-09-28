# Kotlin Spring Boot REST API (Multi-Module)

A clean, modular Kotlin Spring Boot REST API that demonstrates all HTTP methods (GET, POST, PUT, PATCH, DELETE, HEAD, OPTIONS), SOLID layering, and unit + integration tests. Built with Gradle.

Modules:
- domain: Entities and ports (interfaces). Pure Kotlin, no Spring.
- application: Use cases/services. Depends on domain only.
- web: Adapters and configuration. Spring Boot app, REST controllers, in-memory repository implementation, and integration tests.

Tech:
- Kotlin 1.9.x, Spring Boot 3.3.x
- Gradle Kotlin DSL
- JUnit 5, MockK, Spring MockMvc

Quick start
1) Build & test
```bash
./gradlew clean build
```

2) Run the app (web module)
```bash
./gradlew :web:bootRun
```

3) Try the endpoints
- Create
```bash
curl -i -X POST http://localhost:8080/api/items \
  -H 'Content-Type: application/json' \
  -d '{"name":"Alpha","description":"First"}'
```
- List
```bash
curl -i http://localhost:8080/api/items
```
- Get by id
```bash
ID=<copy-from-create-response>
curl -i http://localhost:8080/api/items/$ID
```
- HEAD
```bash
curl -I http://localhost:8080/api/items/$ID
```
- Update (PUT)
```bash
curl -i -X PUT http://localhost:8080/api/items/$ID \
  -H 'Content-Type: application/json' \
  -d '{"name":"Beta","description":"Second"}'
```
- Patch (PATCH)
```bash
curl -i -X PATCH http://localhost:8080/api/items/$ID \
  -H 'Content-Type: application/json' \
  -d '{"description":"Second-Patched"}'
```
- Delete
```bash
curl -i -X DELETE http://localhost:8080/api/items/$ID
```
- Options (collection)
```bash
curl -i -X OPTIONS http://localhost:8080/api/items
```
- Options (item)
```bash
curl -i -X OPTIONS http://localhost:8080/api/items/$ID
```

Structure
- SOLID friendly layering:
  - domain: Item entity + ItemRepository port
  - application: ItemService implements use cases (list, get, create, update, patch, delete, exists)
  - web: Spring wiring (AppConfig), REST controller (ItemController), DTOs, error handling, and an in-memory adapter

Tests
- application: Unit tests for ItemService with MockK
- web: Integration tests with MockMvc covering all HTTP methods and common error cases

Notes
- Requires JDK 17+. Gradle toolchains are configured, so Gradle can provision JDK 17 if necessary.
- The project includes Gradle Wrapper configuration. If the wrapper JAR/scripts are missing execute:
  - If you have Gradle installed: `gradle wrapper`
  - Otherwise, download a matching Gradle wrapper or install Gradle locally (e.g., `brew install gradle`) and re-run the above.

