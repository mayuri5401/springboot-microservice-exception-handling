# Exception Aware User Service

A Spring Boot REST API project focused on implementing **complete exception handling concepts** using a **User Management Service** use case.

## 📌 Project Overview

This project demonstrates how to build a **clean, maintainable, and production-friendly backend API** where all major exception scenarios are handled in a **structured and centralized way**.

The main objective of this project is not only CRUD operations, but to deeply understand and implement **real-world exception handling in Spring Boot**.

> In this project, I tried to cover **almost all important exception handling concepts** used in backend development.

---

## 🚀 Why This Project?

In many beginner-level Spring Boot projects, exception handling is either ignored or handled with scattered `try-catch` blocks.

This project solves that by showing how to:

- Handle exceptions **centrally**
- Return **meaningful API responses**
- Use **custom exceptions**
- Map exceptions to proper **HTTP status codes**
- Keep controllers and services **clean and reusable**
- Improve backend API **maintainability and debugging**

---

## 🎯 Project Objective

The main goal of this project is to demonstrate **exception handling best practices** in Spring Boot using a simple **User Service API**.

### In this project, I tried to cover:

- Custom Exception Handling
- Global Exception Handling using `@RestControllerAdvice`
- Resource Not Found Exception
- Validation Exception Handling
- Bad Request Handling
- Business Rule Exception Handling
- Duplicate Data Exception Handling
- Illegal Argument Exception Handling
- Runtime Exception Handling
- Generic / Fallback Exception Handling
- Standardized Error Response Model
- Proper HTTP Status Code Mapping

---

## 🛠️ Tech Stack

- **Java**
- **Spring Boot**
- **Spring Web**
- **Spring Validation**
- **Maven**
- **REST API**

---

## 📂 Project Structure

```text
src/main/java/com/example/exceptionawareuserservice
│
├── controller
│   └── UserController.java
│
├── service
│   └── UserService.java
│
├── repository
│   └── UserRepository.java
│
├── model / entity
│   └── User.java
│
├── dto
│   └── UserRequest.java / UserResponse.java
│
├── exception
│   ├── UserNotFoundException.java
│   ├── DuplicateUserException.java
│   ├── InvalidUserDataException.java
│   ├── GlobalExceptionHandler.java
│   └── ErrorResponse.java
│
└── ExceptionAwareUserServiceApplication.java
📘 Package Explanation
controller

Handles REST API endpoints and receives client requests.

service

Contains business logic and application rules.

repository

Handles data access operations.

model/entity

Represents the User object and database entity.

dto

Used for request and response payload handling.

exception

Contains:

custom exception classes
global exception handler
structured error response class
🔄 Project Flow
Client sends request to the API
Request reaches the Controller
Controller calls the Service layer
Service executes business logic
If request is valid → success response is returned
If any error occurs:
validation error
resource not found
duplicate user
bad request
runtime exception
The exception is handled by the Global Exception Handler
A clean and structured error response is returned to the client
🧠 High-Level Architecture Diagram
                 ┌──────────────────────┐
                 │      Client / UI     │
                 │  (Postman / Frontend)│
                 └──────────┬───────────┘
                            │ HTTP Request
                            ▼
                 ┌──────────────────────┐
                 │    User Controller   │
                 │  REST API Endpoints  │
                 └──────────┬───────────┘
                            │
                            ▼
                 ┌──────────────────────┐
                 │     User Service     │
                 │ Business Logic Layer │
                 └──────────┬───────────┘
                            │
             ┌──────────────┼──────────────┐
             │              │              │
             ▼              ▼              ▼
 ┌─────────────────┐ ┌─────────────────┐ ┌─────────────────┐
 │ Validation Error│ │ Custom Exception│ │ Runtime / System│
 │ (Bad Input)     │ │ (User Not Found,│ │ Exception       │
 │                 │ │ Duplicate User) │ │                 │
 └────────┬────────┘ └────────┬────────┘ └────────┬────────┘
          │                   │                   │
          └──────────────┬────┴────┬──────────────┘
                         ▼         ▼
               ┌──────────────────────────┐
               │ Global Exception Handler │
               │  @RestControllerAdvice   │
               └──────────┬───────────────┘
                          │
                          ▼
               ┌──────────────────────────┐
               │ Standard Error Response  │
               │ status, message, path,   │
               │ timestamp, error details │
               └──────────────────────────┘
📊 Exception Handling Flow Diagram
                    EXCEPTION HANDLING FLOW

 Request
    │
    ▼
 Controller  ───────────────► Valid Request ─────────► Service Logic ─────────► Success Response
    │
    │
    └────────────► Invalid / Error Scenario
                          │
        ┌─────────────────┼─────────────────┐
        │                 │                 │
        ▼                 ▼                 ▼
 Validation Error   Business Exception   Unexpected Exception
 (e.g. blank name)  (e.g. user not found) (e.g. null pointer)
        │                 │                 │
        └─────────────────┴─────────────────┘
                          │
                          ▼
              Global Exception Handler
             (@RestControllerAdvice)
                          │
                          ▼
                Structured Error Response
🧩 Exception Handling Concepts Covered
1. Custom Exceptions

Custom exceptions are used to represent business-specific errors.

Examples:
UserNotFoundException
DuplicateUserException
InvalidUserDataException
Why?

Because generic exceptions like RuntimeException do not clearly communicate the actual business problem.

Example:
throw new UserNotFoundException("User not found with id: " + id);
2. Global Exception Handling

This project uses a centralized exception handler instead of writing try-catch in every controller.

Implemented using:
@RestControllerAdvice
public class GlobalExceptionHandler {
}
Why?

It keeps the code:

clean
reusable
easy to maintain
production-friendly
3. Validation Exception Handling

Validation is used to ensure the request body contains valid data.

Examples:
blank name
invalid email
null required fields
invalid phone number length
Common validation annotations:
@NotBlank
@Email
@NotNull
@Size
Triggered using:
@Valid
4. Resource Not Found Exception

Used when the requested user does not exist.

Example:
GET /users/101

If user does not exist:

{
  "status": 404,
  "message": "User not found with id: 101"
}
5. Bad Request Handling

Used when the request sent by the client is invalid.

Examples:
malformed JSON
wrong request format
invalid parameter type
missing required request body fields
HTTP Status:

400 Bad Request

6. Business Rule / Duplicate Data Exception

Used when a business rule is violated.

Examples:
duplicate email
duplicate username
user already exists
invalid business operation
7. Runtime / Generic Exception Handling

Used to catch unexpected exceptions in a safe and controlled way.

Example:
@ExceptionHandler(Exception.class)
Why?

So the API never exposes raw stack traces or unstructured errors to the client.

8. Standardized Error Response Model

One of the most important parts of backend exception handling is returning errors in a consistent JSON structure.

Example:
{
  "timestamp": "2026-04-07T10:15:30",
  "status": 404,
  "error": "Not Found",
  "message": "User not found with id: 5",
  "path": "/users/5"
}
Benefits:
easier frontend integration
easier debugging
better API readability
more professional API design
📦 API Endpoints

Replace these if your actual endpoint names are slightly different.

Method	Endpoint	Description
GET	/users	Get all users
GET	/users/{id}	Get user by ID
POST	/users	Create a new user
PUT	/users/{id}	Update existing user
DELETE	/users/{id}	Delete user by ID
🧪 Sample API Requests & Responses
✅ Create User
Request
POST /users
Sample Request Body
{
  "name": "Mayuri",
  "email": "mayuri@example.com",
  "mobile": "9876543210"
}
Success Response
{
  "id": 1,
  "name": "Mayuri",
  "email": "mayuri@example.com",
  "mobile": "9876543210"
}
❌ Validation Error Example
Invalid Request Body
{
  "name": "",
  "email": "invalid-email",
  "mobile": "123"
}
Response
{
  "timestamp": "2026-04-07T10:15:30",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "details": {
    "name": "must not be blank",
    "email": "must be a well-formed email address",
    "mobile": "size must be between 10 and 10"
  }
}
❌ User Not Found Example
Request
GET /users/999
Response
{
  "timestamp": "2026-04-07T10:15:30",
  "status": 404,
  "error": "Not Found",
  "message": "User not found with id: 999",
  "path": "/users/999"
}
❌ Duplicate User Example
Scenario

Trying to create a user with an email that already exists.

Response
{
  "timestamp": "2026-04-07T10:15:30",
  "status": 409,
  "error": "Conflict",
  "message": "User with this email already exists",
  "path": "/users"
}
❌ Generic Internal Server Error Example
Response
{
  "timestamp": "2026-04-07T10:15:30",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Something went wrong. Please try again later.",
  "path": "/users"
}
📋 Exception-to-HTTP Status Mapping
Exception Type	HTTP Status Code
UserNotFoundException	404 Not Found
DuplicateUserException	409 Conflict
InvalidUserDataException	400 Bad Request
Validation Exceptions	400 Bad Request
IllegalArgumentException	400 Bad Request
Generic Runtime Exception	500 Internal Server Error
Fallback Exception	500 Internal Server Error
