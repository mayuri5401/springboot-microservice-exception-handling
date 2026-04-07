package com.UserService.exception;

/**
 * CUSTOM EXCEPTION - DataAccessException
 * ────────────────────────────────────────
 * WHY: Thrown when a database operation fails (save, fetch, delete).
 * WHERE THROWN: UserService.getAllUsers(), getUserById(), createUser(), updateUser(), deleteUser()
 * WHERE CAUGHT: GlobalExceptionHandler.handleDatabaseError()
 * HTTP RESPONSE: 500 INTERNAL SERVER ERROR
 *
 * CONCEPT: Wraps low-level DB exceptions so the controller layer
 * never sees raw JPA/Hibernate errors. Accepts a cause (Throwable)
 * to preserve the original exception stack trace for debugging.
 */
public class DataAccessException extends RuntimeException {

    // Used when only a message is needed
    public DataAccessException(String message) {
        super(message);
    }

    // Used when wrapping a caught DB exception (preserves root cause)
    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
