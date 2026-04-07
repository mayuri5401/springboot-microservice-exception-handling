# Exception-Aware User Service - Complete Documentation

> **One file. Everything you need to understand, run, test, and explain this project.**

---

## Table of Contents

1. [Project Overview](#1-project-overview)
2. [Tech Stack](#2-tech-stack)
3. [Project Structure](#3-project-structure)
4. [File-by-File Explanation](#4-file-by-file-explanation)
5. [High-Level Architecture Diagram](#5-high-level-architecture-diagram)
6. [Exception Handling Concept](#6-exception-handling-concept)
7. [Exception Flow Diagram](#7-exception-flow-diagram)
8. [Detailed Scenarios Step-by-Step](#8-detailed-scenarios-step-by-step)
9. [Exception Decision Tree](#9-exception-decision-tree)
10. [Exception Quick Reference Table](#10-exception-quick-reference-table)
11. [Code Path Reference](#11-code-path-reference)
12. [Complete Exception Checklist](#12-complete-exception-checklist)
13. [API Endpoints](#13-api-endpoints)
14. [Test Cases with curl](#14-test-cases-with-curl)
15. [H2 Database Console](#15-h2-database-console)
16. [Run the Project](#16-run-the-project)
17. [Common Issues & Solutions](#17-common-issues--solutions)
18. [Interview Explanations](#18-interview-explanations)
19. [Key Concepts Summary](#19-key-concepts-summary)
20. [Advanced Topics](#20-advanced-topics)
21. [Reference Documentation](#21-reference-documentation)

---

# Exception-Aware User Service - Help & Documentation

## Quick Start

### Run the Application
```bash
cd /Users/322367/Projects/MY-PROJECT/exception-aware-user-service
./mvnw spring-boot:run
```

Application runs on: `http://localhost:8081`

### Kill Port if Busy
```bash
lsof -ti:8081 | xargs kill -9
```

---

## Exception Handling Architecture

This project demonstrates **centralized exception handling** using Spring Boot's `@RestControllerAdvice`.

### Core Components

#### 1. Custom Exceptions
- `InvalidInputException` - Client validation errors (400 BAD_REQUEST)
- `UserNotFoundException` - Resource not found (404 NOT_FOUND)
- `DataAccessException` - Database operation failures (500 INTERNAL_SERVER_ERROR)

#### 2. Global Exception Handler
File: `src/main/java/com/UserService/exception/GlobalExceptionHandler.java`
- Intercepts all exceptions from `@RestController` methods
- Maps custom exceptions to HTTP status codes
- Returns standardized `ErrorResponse` JSON

#### 3. Standard Error Response
```json
{
  "message": "Error description",
  "status": 400,
  "localDateTime": "2026-04-07T14:30:00"
}
```

---

## Exception Flow Diagram

```
HTTP Request
    ↓
UserController (no try-catch)
    ↓
UserService (throws custom exceptions)
    ↓
GlobalExceptionHandler @RestControllerAdvice
    ↓
ErrorResponse JSON → Client
```

---

## Detailed Exception Handling Flow (Step-by-Step)

### Scenario 1: UserNotFoundException (404 NOT FOUND)

**STEP 1: Client Makes Request**
```bash
curl -X GET http://localhost:8081/api/users/999
```

**STEP 2: UserController Receives Request**
```java
@GetMapping("/{id}")
public ResponseEntity<User> getUserById(@PathVariable Long id) {
    return ResponseEntity.ok(userService.getUserById(id));  // calls service
}
```

**STEP 3: UserService Throws Exception**
```java
public User getUserById(Long id) {
    if (id == null || id <= 0) {
        throw new InvalidInputException("User ID must be greater than zero");
    }
    try {
        return userRepository.findById(id)
                .orElseThrow(() -> 
                    new UserNotFoundException("User not found with id: " + id));  // EXCEPTION THROWN HERE
    } catch (UserNotFoundException e) {
        throw e;  // re-throw for GlobalExceptionHandler
    }
}
```

**STEP 4: GlobalExceptionHandler Catches Exception**
```java
@ExceptionHandler(UserNotFoundException.class)
public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(e.getMessage(), 404, LocalDateTime.now()));
}
```

**STEP 5: Error Response Sent to Client**
```json
HTTP 404 NOT FOUND
{
  "message": "User not found with id: 999",
  "status": 404,
  "localDateTime": "2026-04-07T14:30:00"
}
```

---

### Scenario 2: InvalidInputException (400 BAD REQUEST)

**STEP 1: Client Makes Request with Invalid Data**
```bash
curl -X POST http://localhost:8081/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"","email":""}'
```

**STEP 2: UserController Receives Request**
```java
@PostMapping
public ResponseEntity<User> createUser(@RequestBody User user) {
    return ResponseEntity.status(HttpStatus.CREATED)
            .body(userService.createUser(user));  // calls service
}
```

**STEP 3: UserService Validates Input & Throws Exception**
```java
public User createUser(User user) {
    if (user.getName() == null || user.getName().isBlank()) {
        throw new InvalidInputException("Name cannot be empty");  // EXCEPTION THROWN HERE
    }
    if (user.getEmail() == null || user.getEmail().isBlank()) {
        throw new InvalidInputException("Email cannot be empty");  // OR HERE
    }
    // ... rest of method
}
```

**STEP 4: GlobalExceptionHandler Catches Exception**
```java
@ExceptionHandler(InvalidInputException.class)
public ResponseEntity<ErrorResponse> handleInvalidInput(InvalidInputException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(e.getMessage(), 400, LocalDateTime.now()));
}
```

**STEP 5: Error Response Sent to Client**
```json
HTTP 400 BAD REQUEST
{
  "message": "Name cannot be empty",
  "status": 400,
  "localDateTime": "2026-04-07T14:30:00"
}
```

---

### Scenario 3: DataAccessException (500 INTERNAL SERVER ERROR)

**STEP 1: Client Makes Request**
```bash
curl -X GET http://localhost:8081/api/users
```

**STEP 2: UserController Receives Request**
```java
@GetMapping
public ResponseEntity<List<User>> getAllUsers() {
    return ResponseEntity.ok(userService.getAllUsers());  // calls service
}
```

**STEP 3: UserService Tries DB Operation & Catches Error**
```java
public List<User> getAllUsers() {
    try {
        return userRepository.findAll();  // DB operation
    } catch (Exception e) {
        throw new DataAccessException("Failed to fetch users from database", e);  // EXCEPTION THROWN HERE
    }
}
```

**STEP 4: GlobalExceptionHandler Catches Exception**
```java
@ExceptionHandler(DataAccessException.class)
public ResponseEntity<ErrorResponse> handleDatabaseError(DataAccessException e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse(e.getMessage(), 500, LocalDateTime.now()));
}
```

**STEP 5: Error Response Sent to Client**
```json
HTTP 500 INTERNAL SERVER ERROR
{
  "message": "Failed to fetch users from database",
  "status": 500,
  "localDateTime": "2026-04-07T14:30:00"
}
```

---

### Scenario 4: Generic Exception (500 Catch-All)

**STEP 1: Client Makes Request to Test Endpoint**
```bash
curl -X GET http://localhost:8081/api/users/test-error
```

**STEP 2: UserController Throws Unexpected Error**
```java
@GetMapping("/test-error")
public ResponseEntity<String> testGenericError() {
    throw new RuntimeException("Simulated unexpected server error");  // EXCEPTION THROWN HERE
}
```

**STEP 3: GlobalExceptionHandler Catches Fallback Exception**
```java
@ExceptionHandler(Exception.class)  // catches ANY exception not handled above
public ResponseEntity<ErrorResponse> handleAllExceptions(Exception e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse("Internal server error: " + e.getMessage(), 500, LocalDateTime.now()));
}
```

**STEP 4: Error Response Sent to Client**
```json
HTTP 500 INTERNAL SERVER ERROR
{
  "message": "Internal server error: Simulated unexpected server error",
  "status": 500,
  "localDateTime": "2026-04-07T14:30:00"
}
```

---

## Exception Handling in This Project

### Where Exceptions Are Thrown

**UserService.java** (`src/main/java/com/UserService/service/`)

```
invalidUser(Input) → InvalidInputException → 400 BAD_REQUEST
userNotFound(ID)  → UserNotFoundException → 404 NOT_FOUND
dbFailure()       → DataAccessException → 500 INTERNAL_SERVER_ERROR
unexpected()      → Exception (fallback) → 500 INTERNAL_SERVER_ERROR
```

### Where Exceptions Are Caught

**GlobalExceptionHandler.java** (`src/main/java/com/UserService/exception/`)

Four handler methods:
1. `handleInvalidInput()` → `InvalidInputException` → 400
2. `handleUserNotFound()` → `UserNotFoundException` → 404
3. `handleDatabaseError()` → `DataAccessException` → 500
4. `handleAllExceptions()` → `Exception` (catch-all) → 500

---

## Quick Reference: Exception Flow Summary

| # | Scenario | Request | Service Check | Exception Thrown | Handler | HTTP Status | Response |
|---|----------|---------|----------------|------------------|---------|------------|----------|
| 1 | User doesn't exist | `GET /api/users/999` | `id found?` | `UserNotFoundException` | `handleUserNotFound()` | 404 | User not found with id: 999 |
| 2 | Invalid ID (negative) | `GET /api/users/-1` | `id > 0?` | `InvalidInputException` | `handleInvalidInput()` | 400 | User ID must be greater than zero |
| 3 | Blank name field | `POST /api/users` body: `{"name":""}` | `name blank?` | `InvalidInputException` | `handleInvalidInput()` | 400 | Name cannot be empty |
| 4 | Blank email field | `POST /api/users` body: `{"email":""}` | `email blank?` | `InvalidInputException` | `handleInvalidInput()` | 400 | Email cannot be empty |
| 5 | DB operation fails | `GET /api/users` (DB down) | `DB accessible?` | `DataAccessException` | `handleDatabaseError()` | 500 | Failed to fetch users from database |
| 6 | Unexpected error | `GET /api/users/test-error` | `none` | `RuntimeException` | `handleAllExceptions()` | 500 | Internal server error: ... |

---

## Exception Flow: Code Path Reference

To help you trace through the code:

### Path for InvalidInputException
```
UserController.getUserById(id)
  ↓
UserService.getUserById(id)
  ↓
[Validation Check: if (id <= 0)] ← Exception thrown here
  ↓
GlobalExceptionHandler.handleInvalidInput()
  ↓
ErrorResponse (400 BAD_REQUEST)
```

### Path for UserNotFoundException
```
UserController.getUserById(id)
  ↓
UserService.getUserById(id)
  ↓
[Validation Check: if (id > 0)] ← Passes
  ↓
UserRepository.findById(id)
  ↓
[.orElseThrow()] ← Exception thrown here if not found
  ↓
GlobalExceptionHandler.handleUserNotFound()
  ↓
ErrorResponse (404 NOT_FOUND)
```

### Path for DataAccessException
```
UserController.getAllUsers()
  ↓
UserService.getAllUsers()
  ↓
try {
  UserRepository.findAll() ← DB call
} catch (Exception e) {
  throw DataAccessException ← Exception wrapped here
}
  ↓
GlobalExceptionHandler.handleDatabaseError()
  ↓
ErrorResponse (500 INTERNAL_SERVER_ERROR)
```

### Path for Generic Exception (Catch-All)
```
UserController.testGenericError()
  ↓
throw RuntimeException() ← Any unexpected exception
  ↓
GlobalExceptionHandler.handleAllExceptions()
  ↓
ErrorResponse (500 INTERNAL_SERVER_ERROR)
```

---

## When Each Exception Is Thrown - Complete Checklist

### InvalidInputException (400 BAD_REQUEST)
- [ ] When creating user with blank `name` field
- [ ] When creating user with blank `email` field
- [ ] When updating user with blank `name` field
- [ ] When updating user with blank `email` field
- [ ] When getting user with ID = 0
- [ ] When getting user with ID < 0 (negative)
- [ ] When deleting user with ID = 0
- [ ] When deleting user with ID < 0 (negative)

### UserNotFoundException (404 NOT_FOUND)
- [ ] When getting user with non-existent ID
- [ ] When updating user with non-existent ID
- [ ] When deleting user with non-existent ID

### DataAccessException (500 INTERNAL_SERVER_ERROR)
- [ ] When database is down/unreachable
- [ ] When repository `.save()` fails
- [ ] When repository `.findAll()` fails
- [ ] When repository `.findById()` fails
- [ ] When repository `.deleteById()` fails

### Generic Exception (500 INTERNAL_SERVER_ERROR)
- [ ] When `/api/users/test-error` endpoint is called (intentional test)
- [ ] When ANY unexpected runtime exception occurs

---

## How to Explain This in an Interview

### Simple Version (30 seconds)
"This project uses centralized exception handling. The service layer throws custom exceptions, and `GlobalExceptionHandler` catches them, maps them to HTTP status codes, and returns a standard `ErrorResponse` JSON. This keeps controllers clean and makes error handling consistent."

### Medium Version (2 minutes)
"The architecture follows a 3-layer pattern:
1. **Controller layer** - receives HTTP request, calls service (no try-catch)
2. **Service layer** - contains business logic, throws custom exceptions (InvalidInputException, UserNotFoundException, DataAccessException)
3. **Handler layer** - `@RestControllerAdvice` intercepts exceptions and maps them to HTTP responses

Every exception type has its own handler method. For example, `UserNotFoundException` returns 404, `InvalidInputException` returns 400, and database errors return 500. All responses follow the same `ErrorResponse` format with message, status, and timestamp."

### Long Version (5 minutes - Full explanation)
"The project demonstrates clean exception handling using Spring's `@RestControllerAdvice`. Here's the flow:

When a client makes a request, it hits the controller. The controller doesn't handle exceptions directly—it just calls the service. The service performs validation and business logic, and if something goes wrong, it throws a custom exception.

There are three custom exceptions:
1. **InvalidInputException** - thrown when user input is invalid (blank fields, invalid ID)
2. **UserNotFoundException** - thrown when a user doesn't exist in the database
3. **DataAccessException** - thrown when a database operation fails

The `GlobalExceptionHandler` annotated with `@RestControllerAdvice` intercepts all these exceptions. Each exception type has its own handler method:
- `handleInvalidInput()` → returns 400 BAD_REQUEST
- `handleUserNotFound()` → returns 404 NOT_FOUND
- `handleDatabaseError()` → returns 500 INTERNAL_SERVER_ERROR
- `handleAllExceptions()` → returns 500 (catch-all for unexpected errors)

Every handler returns an `ErrorResponse` object with three fields: message (error description), status (HTTP code), and localDateTime (when error occurred).

The benefits are:
- Controllers stay clean (focused on happy path)
- All errors follow the same response format
- Easy to maintain (change behavior in one place)
- Clear separation of concerns"

---

### Success Cases

```bash
# Get all users
curl -X GET http://localhost:8081/api/users

# Get user by ID (exists)
curl -X GET http://localhost:8081/api/users/1

# Create user
curl -X POST http://localhost:8081/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe","email":"john@example.com"}'

# Update user
curl -X PUT http://localhost:8081/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Jane Doe","email":"jane@example.com"}'

# Delete user
curl -X DELETE http://localhost:8081/api/users/1

# Home endpoint
curl -X GET http://localhost:8081/
```

### Exception Test Cases

```bash
# InvalidInputException (400) - User ID <= 0
curl -X GET http://localhost:8081/api/users/-1
curl -X GET http://localhost:8081/api/users/0

# InvalidInputException (400) - Blank fields
curl -X POST http://localhost:8081/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"","email":""}'

# UserNotFoundException (404) - User doesn't exist
curl -X GET http://localhost:8081/api/users/999

# Generic Exception (500) - Test catch-all handler
curl -X GET http://localhost:8081/api/users/test-error
```

---

## H2 Database Console

Access in-memory H2 database:
- URL: `http://localhost:8081/h2-console`
- JDBC URL: `jdbc:h2:mem:userdb`
- Username: `sa`
- Password: *(leave empty)*

---

## Project Dependencies

- **Spring Web** - REST API support
- **Spring Data JPA** - Database abstraction
- **Spring Validation** - Input validation
- **H2 Database** - In-memory DB for development
- **Lombok** - Boilerplate reduction (optional)

---

## Project Structure

```
exception-aware-user-service/
├── src/main/java/com/UserService/
│   ├── Controller/
│   │   └── UserController.java
│   ├── service/
│   │   └── UserService.java
│   ├── repository/
│   │   └── UserRepository.java
│   ├── model/
│   │   └── User.java
│   ├── exception/
│   │   ├── GlobalExceptionHandler.java
│   │   ├── ErrorResponse.java
│   │   ├── InvalidInputException.java
│   │   ├── UserNotFoundException.java
│   │   └── DataAccessException.java
│   └── UserServiceApplication.java
├── src/main/resources/
│   └── application.properties (H2 config)
├── pom.xml
└── README.md
```

---

## Key Concepts: Why This Approach?

1. **Centralized Handling** - No repetitive try-catch in every controller method
2. **Consistent Response** - All errors follow same `ErrorResponse` format
3. **Clean Code** - Controllers remain focused on happy path
4. **Easy Maintenance** - Change error behavior in one place
5. **Service-Layer Exceptions** - Business logic separated from transport layer

---

## Exception Mapping Reference

| Exception | Thrown When | HTTP Status | Handler Method |
|---|---|---|---|
| `InvalidInputException` | Blank name/email, ID <= 0 | 400 | `handleInvalidInput()` |
| `UserNotFoundException` | User ID not found in DB | 404 | `handleUserNotFound()` |
| `DataAccessException` | DB operation fails | 500 | `handleDatabaseError()` |
| `Exception` (generic) | Unhandled runtime exception | 500 | `handleAllExceptions()` |

---

## Common Issues & Solutions

### Port Already in Use
```bash
lsof -ti:8081 | xargs kill -9
# Then restart: ./mvnw spring-boot:run
```

### H2 Console Not Loading
- Verify `spring.h2.console.enabled=true` in `application.properties`
- Check URL: `http://localhost:8081/h2-console`

### 404 on Root Path
- Use `http://localhost:8081/` (should return "User Service is running")
- API endpoints use `/api/users`

---

## Testing with Postman

1. Import collection: `UserService.postman_collection.json`
2. Set base URL variable: `{{baseUrl}} = http://localhost:8081`
3. Run requests in order: success first, then exception tests

---

## Reference Documentation

- [Spring Boot Official Docs](https://docs.spring.io/spring-boot/4.0.5/reference/)
- [Spring Web Docs](https://docs.spring.io/spring-boot/4.0.5/reference/web/servlet.html)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [REST Best Practices](https://spring.io/guides/tutorials/rest/)

---

## Next Steps / Advanced Topics

- Add `@Valid` annotation and handle `MethodArgumentNotValidException`
- Implement `PATCH /api/users/{id}` for partial updates
- Add correlation IDs to error responses for debugging
- Implement async exception handling with `@Async`
- Add structured logging with error tracking
- Implement error codes (e.g., `USER_NOT_FOUND`, `INVALID_INPUT`)

---

## Support & Questions

For questions about exception handling in this project:
1. Check comments in `GlobalExceptionHandler.java`
2. Review `UserService.java` for where exceptions are thrown
3. See `ErrorResponse.java` for response structure
4. Run test cases from **Exception Test Cases** section above

---

Generated: April 7, 2026
