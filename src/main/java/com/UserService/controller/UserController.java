package com.UserService.controller;

import com.UserService.model.User;
import com.UserService.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * USER CONTROLLER
 * ─────────────────
 * CONCEPT: The controller does NOT handle any exceptions directly.
 * It simply calls the service and returns the result.
 * All exceptions thrown by UserService bubble up to GlobalExceptionHandler automatically.
 *
 * EXCEPTION FLOW PER ENDPOINT:
 *   GET  /api/users          -> DataAccessException (DB fails)
 *   GET  /api/users/{id}     -> InvalidInputException (id<=0), UserNotFoundException (not found)
 *   POST /api/users          -> InvalidInputException (blank fields), DataAccessException (DB fails)
 *   PUT  /api/users/{id}     -> InvalidInputException, UserNotFoundException, DataAccessException
 *   DELETE /api/users/{id}   -> InvalidInputException (id<=0), UserNotFoundException (not found)
 *   GET  /api/users/test-error -> Exception (generic catch-all test)
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Returns all users | Exception: DataAccessException -> 500
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Returns user by ID | Exception: InvalidInputException -> 400, UserNotFoundException -> 404
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // Creates a new user | Exception: InvalidInputException -> 400, DataAccessException -> 500
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }

    // Updates existing user | Exception: InvalidInputException -> 400, UserNotFoundException -> 404, DataAccessException -> 500
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    // Deletes user by ID | Exception: InvalidInputException -> 400, UserNotFoundException -> 404
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    // Test endpoint to trigger handleAllExceptions() in GlobalExceptionHandler -> 500
    // Call: GET /api/users/test-error
    @GetMapping("/test-error")
    public ResponseEntity<String> testGenericError() {
        throw new RuntimeException("Simulated unexpected server error");
    }
}
