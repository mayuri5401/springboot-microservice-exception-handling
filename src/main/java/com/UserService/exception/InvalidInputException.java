package com.UserService.exception;

/**
 * CUSTOM EXCEPTION - InvalidInputException
 * ─────────────────────────────────────────
 * WHY: Thrown when user provides invalid or blank input data.
 * WHERE THROWN: UserService.createUser(), updateUser(), getUserById(), deleteUser()
 * WHERE CAUGHT: GlobalExceptionHandler.handleInvalidInput()
 * HTTP RESPONSE: 400 BAD REQUEST
 *
 * CONCEPT: RuntimeException (unchecked) - recommended for service-layer
 * validation so Spring can automatically route it to GlobalExceptionHandler.
 */
public class InvalidInputException extends RuntimeException {
    public InvalidInputException(String message) {
        super(message);
    }
}
