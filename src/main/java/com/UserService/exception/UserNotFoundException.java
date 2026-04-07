package com.UserService.exception;

/**
 * CUSTOM EXCEPTION - UserNotFoundException
 * ─────────────────────────────────────────
 * WHY: Thrown when a user is not found in the database by ID.
 * WHERE THROWN: UserService.getUserById(), updateUser(), deleteUser()
 * WHERE CAUGHT: GlobalExceptionHandler.handleUserNotFound()
 * HTTP RESPONSE: 404 NOT FOUND
 *
 * CONCEPT: Extend RuntimeException so we don't need to declare
 * it in method signatures (unchecked exception).
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
