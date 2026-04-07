package com.UserService.exception;

import java.time.LocalDateTime;

/**
 * ERROR RESPONSE - ErrorResponse (DTO)
 * ──────────────────────────────────────
 * WHY: A standard JSON structure returned to the client whenever
 *      an exception is caught by GlobalExceptionHandler.
 * USED IN: GlobalExceptionHandler - all 4 handler methods
 *
 * CONCEPT: Instead of returning Spring's default Whitelabel error page,
 * we return a clean, structured JSON body with:
 *   - message      : human-readable error description
 *   - status       : HTTP status code (400, 404, 500)
 *   - localDateTime: timestamp when the error occurred
 *
 * EXAMPLE RESPONSE:
 * {
 *   "message": "User not found with id: 99",
 *   "status": 404,
 *   "localDateTime": "2026-04-07T14:30:00"
 * }
 */
public class ErrorResponse {
    private String message;
    private int status;
    private LocalDateTime localDateTime;

    public ErrorResponse(String message, int status, LocalDateTime localDateTime) {
        this.message = message;
        this.status = status;
        this.localDateTime = localDateTime;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }
}
