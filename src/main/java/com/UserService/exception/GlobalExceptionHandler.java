package com.UserService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

/**
 * GLOBAL EXCEPTION HANDLER
 * ─────────────────────────
 * WHY: Centralizes all exception handling in one place.
 *      Without this, every controller method would need its own try-catch block.
 *
 * HOW IT WORKS:
 *   @RestControllerAdvice = @ControllerAdvice + @ResponseBody
 *   It intercepts exceptions thrown from ANY @RestController in the project
 *   and maps them to a structured ErrorResponse JSON.
 *
 * FLOW:
 *   HTTP Request
 *       -> UserController (calls service)
 *       -> UserService    (throws custom exception)
 *       -> GlobalExceptionHandler (catches it here)
 *       -> ErrorResponse  (returned as JSON to client)
 *
 * HANDLERS SUMMARY:
 *   handleUserNotFound()   -> UserNotFoundException  -> 404 NOT FOUND
 *   handleInvalidInput()   -> InvalidInputException  -> 400 BAD REQUEST
 *   handleDatabaseError()  -> DataAccessException    -> 500 INTERNAL SERVER ERROR
 *   handleAllExceptions()  -> Exception (catch-all)  -> 500 INTERNAL SERVER ERROR
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles: UserNotFoundException
     * Triggered when: getUserById() / updateUser() / deleteUser() - user ID not found in DB
     * Returns: 404 NOT FOUND
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(e.getMessage(), 404, LocalDateTime.now()));
    }

    /**
     * Handles: InvalidInputException
     * Triggered when: blank name/email in createUser()/updateUser() or ID <= 0
     * Returns: 400 BAD REQUEST
     */
    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorResponse> handleInvalidInput(InvalidInputException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage(), 400, LocalDateTime.now()));
    }

    /**
     * Handles: DataAccessException
     * Triggered when: any DB operation fails (findAll, save, deleteById)
     * Returns: 500 INTERNAL SERVER ERROR
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDatabaseError(DataAccessException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(e.getMessage(), 500, LocalDateTime.now()));
    }

    /**
     * Handles: Any other unexpected Exception (catch-all safety net)
     * Triggered when: GET /api/users/test-error or any unhandled runtime exception
     * Returns: 500 INTERNAL SERVER ERROR
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Internal server error: " + e.getMessage(), 500, LocalDateTime.now()));
    }
}
