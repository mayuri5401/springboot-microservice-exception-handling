# Microservice Exception Handling Guide

A **Spring Boot REST API project** designed to demonstrate **end-to-end exception handling concepts** used in **real-world backend and microservice applications**.

This project is built around a **User Management Service** and focuses on implementing **clean, centralized, scalable, and production-friendly exception handling**.

> In this project, I tried to cover **almost all major exception handling concepts** that are commonly asked in **Java / Spring Boot / Microservice interviews**.

---

# 📌 Project Overview

This project demonstrates how to handle exceptions in a **structured and reusable way** in a Spring Boot application.

Instead of allowing the application to return default or unclear errors, this project ensures that every exception is handled properly and transformed into a **meaningful API response**.

# 📂 Project Structure

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

Handles incoming REST API requests.

service

Contains business logic and validations.

repository

Handles persistence / data access logic.

model/entity

Represents domain entity such as User.

dto

Used for request/response payload transfer.

exception

Contains:

custom exceptions
global exception handler
structured error response model
🔄 Project Flow
Client sends request to the User API
Request reaches the Controller
Controller calls the Service layer
Service executes business logic
If request is valid → success response is returned
If any error occurs:
validation error
resource not found
duplicate user
invalid request
runtime exception
The exception is intercepted by the Global Exception Handler
A clean and standardized error response is returned
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
🧩 Exception Handling Concepts Covered in This Project
1. Custom Exceptions

Custom exceptions are created to represent business-specific problems.

Examples:
UserNotFoundException
DuplicateUserException
InvalidUserDataException
Example:
throw new UserNotFoundException("User not found with id: " + id);
2. Global Exception Handling

Instead of writing try-catch in every controller, exception handling is centralized using:

@RestControllerAdvice
public class GlobalExceptionHandler {
}
Benefits:
clean code
reusable handling
centralized error logic
better maintainability
3. Validation Exception Handling

Handles invalid request payloads using:

@Valid

and annotations like:

@NotBlank
@NotNull
@Email
@Size
Examples:
empty name
invalid email
null values
invalid mobile number
4. Resource Not Found Exception

Used when requested data does not exist.

Example:
GET /users/101
Response:
{
  "status": 404,
  "message": "User not found with id: 101"
}
5. Bad Request Handling

Used when request is invalid.

Examples:
malformed JSON
wrong request format
invalid parameter type
missing required field
6. Duplicate / Business Rule Exception

Used when a business rule is violated.

Examples:
duplicate email
duplicate username
already existing user
7. Runtime / Generic Exception Handling

Fallback exception handling for unexpected errors.

@ExceptionHandler(Exception.class)
Why?

To avoid exposing raw stack traces and internal implementation details.

8. Standardized Error Response Model

All API errors are returned in a common structure.

Example:
{
  "timestamp": "2026-04-07T10:15:30",
  "status": 404,
  "error": "Not Found",
  "message": "User not found with id: 5",
  "path": "/users/5"
}
📦 API Endpoints

Update these if your actual endpoints are slightly different.

Method	Endpoint	Description
GET	/users	Get all users
GET	/users/{id}	Get user by ID
POST	/users	Create new user
PUT	/users/{id}	Update user
DELETE	/users/{id}	Delete user
🧪 Sample API Requests & Responses
✅ Create User
Request
POST /users
Request Body
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
Response
{
  "timestamp": "2026-04-07T10:15:30",
  "status": 409,
  "error": "Conflict",
  "message": "User with this email already exists",
  "path": "/users"
}
❌ Internal Server Error Example
Response
{
  "timestamp": "2026-04-07T10:15:30",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Something went wrong. Please try again later.",
  "path": "/users"
}
📋 Exception to HTTP Status Mapping
Exception Type	HTTP Status Code
UserNotFoundException	404 Not Found
DuplicateUserException	409 Conflict
InvalidUserDataException	400 Bad Request
Validation Exceptions	400 Bad Request
IllegalArgumentException	400 Bad Request
Generic Runtime Exception	500 Internal Server Error
Fallback Exception	500 Internal Server Error
🧪 How to Run the Project
1. Clone the repository
git clone https://github.com/mayuri5401/exception-aware-user-service.git
2. Navigate to project folder
cd exception-aware-user-service
3. Build the project
mvn clean install
4. Run the application
mvn spring-boot:run
5. Access API
http://localhost:8080
▶️ How to Test

You can test the API using:

Postman
cURL
Swagger (if added later)
frontend application
Suggested test scenarios:
create valid user
create invalid user
fetch existing user
fetch non-existing user
create duplicate user
send malformed request body
trigger generic exception
🎯 Exception Handling Interview Revision Notes (Java Developer - 4.5 Years)

This section is added as a quick revision guide for Java / Spring Boot / Microservice interviews.

1️⃣ Exception Handling in Java
What is Exception Handling?

Exception handling is a mechanism in Java used to handle runtime problems in a controlled way so that the application does not terminate unexpectedly.

Why do we need it?
prevents application crash
improves maintainability
provides meaningful error messages
separates normal flow from failure handling
What is an Exception?

An exception is an unexpected event that interrupts the normal flow of program execution.

Examples:
divide by zero
null pointer access
invalid input
file not found
database failure
2️⃣ Types of Exceptions in Java
Checked Exceptions

Checked at compile time.

Examples:
IOException
SQLException
FileNotFoundException
Must be:
handled using try-catch
or declared using throws
Unchecked Exceptions

Occur at runtime.

Examples:
NullPointerException
ArithmeticException
IllegalArgumentException
ArrayIndexOutOfBoundsException
Errors

Serious system-level issues.

Examples:
OutOfMemoryError
StackOverflowError
3️⃣ Java Exception Hierarchy
Throwable
├── Error
└── Exception
    ├── Checked Exceptions
    └── RuntimeException
        ├── NullPointerException
        ├── ArithmeticException
        ├── IllegalArgumentException
        └── ArrayIndexOutOfBoundsException
4️⃣ Keywords Used in Exception Handling
try

Wraps risky code.

catch

Handles exception.

finally

Executes cleanup logic.

throw

Explicitly throws exception.

throws

Declares exception in method signature.

5️⃣ Difference Between throw and throws
throw	throws
Used to explicitly throw an exception	Used to declare exceptions
Used inside method	Used in method signature
Throws one exception object	Can declare multiple exceptions
Example:
throw new IllegalArgumentException("Invalid age");
public void readFile() throws IOException {
}
6️⃣ Checked vs Unchecked Exceptions
Checked Exception	Unchecked Exception
Compile-time checked	Runtime exception
Must be handled	Optional to handle
Extends Exception	Extends RuntimeException
Example: IOException	Example: NullPointerException
7️⃣ What is Custom Exception?

A custom exception is a user-defined exception created to represent a specific business problem.

Example:
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
Real examples:
UserNotFoundException
OrderNotFoundException
PaymentFailedException
DuplicateUserException
8️⃣ Why Prefer Custom Exceptions in Real Projects?
Benefits:
better readability
better business representation
easier debugging
cleaner service code
easier exception mapping
9️⃣ Best Practice: Checked vs Unchecked in Backend Projects

In modern Spring Boot projects, business/API exceptions are usually created as:

extends RuntimeException
Why?
less boilerplate
cleaner code
works well with global exception handling
🔟 What is Exception Propagation?

Exception propagation means an exception thrown in one method moves upward through the call stack until it is handled.

Example:
controller -> service -> repository

If repository throws exception and service does not handle it, it propagates upward.

1️⃣1️⃣ What is Stack Trace?

A stack trace shows:

where exception occurred
method call sequence
line number
Important:

Useful for debugging, but should not be exposed in API responses.

1️⃣2️⃣ Difference Between final, finally, and finalize()
Keyword	Meaning
final	constant / non-overridable / non-inheritable
finally	cleanup block
finalize()	old GC-related method
1️⃣3️⃣ What Happens if Exception is Not Handled?

If not handled:

it propagates upward
JVM / request flow may terminate
web app may return 500 Internal Server Error
1️⃣4️⃣ Why Avoid Too Many try-catch Blocks?

Because it:

makes code messy
reduces readability
duplicates logic
increases maintenance
Better approach:
custom exceptions
global exception handling
centralized API error responses
1️⃣5️⃣ Exception Handling in Spring Boot

Exception handling in Spring Boot can be done at:

1. Local level

Using try-catch

2. Controller level

Using @ExceptionHandler

3. Global level

Using @ControllerAdvice / @RestControllerAdvice

Best practice:
@RestControllerAdvice
1️⃣6️⃣ What is @ExceptionHandler?

Used to handle a specific exception type.

Example:
@ExceptionHandler(UserNotFoundException.class)
public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
}
1️⃣7️⃣ What is @ControllerAdvice?

Used for global exception handling across controllers.

1️⃣8️⃣ Difference Between @ControllerAdvice and @RestControllerAdvice
@ControllerAdvice	@RestControllerAdvice
Used for MVC	Used for REST APIs
May require @ResponseBody	Returns JSON automatically
Better for web pages	Better for backend APIs
1️⃣9️⃣ Why Use Global Exception Handling?
Benefits:
centralized handling
clean controller code
reusable logic
consistent error responses
easier maintenance
2️⃣0️⃣ What is a Standard Error Response?

A common JSON structure used for all API errors.

Example:
{
  "timestamp": "2026-04-07T10:15:30",
  "status": 404,
  "error": "Not Found",
  "message": "User not found with id: 1",
  "path": "/users/1"
}
2️⃣1️⃣ Recommended Fields in Error Response

A good error response usually contains:

timestamp
status
error
message
path
details
traceId (for microservices)
2️⃣2️⃣ Validation Exception Handling

Validation errors happen when request payload is invalid.

Annotations:
@NotBlank
@NotNull
@Email
@Size
@Min
@Max
Trigger:
@Valid
Common exception:
MethodArgumentNotValidException
2️⃣3️⃣ Proper Validation Error Response

Instead of generic messages, return field-wise errors.

Example:
{
  "status": 400,
  "message": "Validation failed",
  "details": {
    "name": "must not be blank",
    "email": "must be a well-formed email address"
  }
}
2️⃣4️⃣ Common Exceptions in Spring Boot APIs

Frequently handled exceptions:

MethodArgumentNotValidException
HttpMessageNotReadableException
MissingServletRequestParameterException
MethodArgumentTypeMismatchException
ConstraintViolationException
IllegalArgumentException
DataIntegrityViolationException
NullPointerException
Exception
2️⃣5️⃣ Difference Between 400, 404, 409, 500
Status Code	Meaning	Use Case
400	Bad Request	Invalid input
404	Not Found	Resource missing
409	Conflict	Duplicate / state conflict
500	Internal Server Error	Unexpected failure
2️⃣6️⃣ When to Use 404 Not Found?

Use when resource does not exist.

Examples:
user not found
order not found
product not found
2️⃣7️⃣ When to Use 400 Bad Request?

Use when request is invalid.

Examples:
invalid JSON
missing field
invalid param
bad request body
2️⃣8️⃣ When to Use 409 Conflict?

Use when request is valid but conflicts with current system state.

Examples:
duplicate email
duplicate username
already processed entity
2️⃣9️⃣ When to Use 500 Internal Server Error?

Use for unexpected server-side issues.

Examples:
null pointer
DB failure
external service issue
coding bug
3️⃣0️⃣ Why Not Return Raw Exception Message Directly?

Because it may:

expose internal implementation details
leak sensitive information
confuse clients
Better:

Return safe, business-friendly error messages.

3️⃣1️⃣ What is ResponseEntityExceptionHandler?

A Spring base class used to customize handling of common framework-level exceptions.

Useful for:

validation failures
request parsing issues
missing parameters
type mismatch
3️⃣2️⃣ What is MethodArgumentNotValidException?

Occurs when request body validation fails.

Example:
@PostMapping
public ResponseEntity<User> createUser(@Valid @RequestBody UserRequest request) {
}
3️⃣3️⃣ What is ConstraintViolationException?

Occurs when validation fails for:

request params
query params
path variables
3️⃣4️⃣ What is HttpMessageNotReadableException?

Occurs when request body cannot be parsed.

Examples:
malformed JSON
wrong data type
3️⃣5️⃣ What is MethodArgumentTypeMismatchException?

Occurs when request parameter or path variable has incorrect type.

Example:
GET /users/abc

If id should be Long, this may trigger type mismatch exception.

3️⃣6️⃣ What is DataIntegrityViolationException?

Common DB constraint exception.

Examples:
unique key violation
foreign key violation
not-null constraint violation

Usually mapped to:

409 Conflict
or 400 Bad Request
3️⃣7️⃣ Best Exception Handling Strategy in Spring Boot
Recommended:
use custom exceptions for business errors
use @RestControllerAdvice
return standardized error JSON
map correct HTTP status
avoid unnecessary try-catch
log internal exceptions
3️⃣8️⃣ Exception Handling in Microservices

In microservices, exception handling becomes more important because services communicate with each other.

Common issues:
downstream service failure
timeout
fallback scenario
distributed debugging difficulty
3️⃣9️⃣ What Should Be Added in Microservice Error Responses?

Recommended additional fields:

serviceName
traceId
correlationId
errorCode
timestamp
path
Example:
{
  "timestamp": "2026-04-07T10:15:30",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Payment service is unavailable",
  "serviceName": "order-service",
  "traceId": "abc123xyz",
  "path": "/orders"
}
4️⃣0️⃣ Technical Exception vs Business Exception
Technical Exception	Business Exception
Related to DB / framework / runtime	Related to business rules
Example: DB timeout	Example: user already exists
Usually unexpected	Usually expected scenario
4️⃣1️⃣ Real-World Best Practices
Best practices:
prefer custom exceptions
use global exception handling
do not expose stack traces
use proper HTTP status codes
keep error response consistent
add logging
use meaningful messages
separate business and technical exceptions
include traceId in microservices
validate request properly
4️⃣2️⃣ Common Interview Questions (Quick Revision)
Q1. What is exception handling in Java?

Exception handling is a mechanism to handle runtime errors in a controlled way without crashing the application.

Q2. Difference between checked and unchecked exceptions?

Checked exceptions are compile-time checked, unchecked exceptions occur at runtime.

Q3. Why use custom exceptions?

To represent business-specific problems clearly and improve maintainability.

Q4. Why use @RestControllerAdvice?

To centralize exception handling and return consistent API responses.

Q5. Difference between throw and throws?

throw explicitly throws an exception; throws declares it in method signature.

Q6. Why avoid too many try-catch blocks?

Because they make code messy and repetitive.

Q7. What is MethodArgumentNotValidException?

It occurs when request body validation fails.

Q8. What is the use of @Valid?

It triggers validation on request payloads.

Q9. Why is standard error response important?

Because clients need predictable and structured error output.

Q10. How is exception handling different in microservices?

Microservices need traceability, service-level consistency, and resilience-aware error handling.

4️⃣3️⃣ Best Short Interview Answer (4–5 Years Experience)

In Spring Boot applications, I generally prefer centralized exception handling using @RestControllerAdvice along with custom exceptions for business scenarios like resource not found, duplicate data, or invalid operations.
For validation and framework-level exceptions, I map them to appropriate HTTP status codes and return a standardized error response containing fields like timestamp, status, message, and path.
In microservices, I also consider adding trace IDs, service names, and proper logging for better observability and debugging.

4️⃣4️⃣ Final Revision Summary

If asked about exception handling in interview, revise these key points:

Core Java
checked vs unchecked
throw vs throws
try-catch-finally
custom exceptions
exception propagation
Spring Boot
@ExceptionHandler
@ControllerAdvice
@RestControllerAdvice
validation exception handling
standard error response
proper HTTP status mapping
Microservices
service-to-service failures
error consistency
logging
traceId / correlationId
resilience and fallback
