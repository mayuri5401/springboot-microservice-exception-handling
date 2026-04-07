package com.UserService.service;

import com.UserService.exception.DataAccessException;
import com.UserService.exception.InvalidInputException;
import com.UserService.exception.UserNotFoundException;
import com.UserService.model.User;
import com.UserService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * USER SERVICE
 * ─────────────
 * CONCEPT: This is where ALL custom exceptions are thrown.
 * The service layer is responsible for:
 *   1. Input validation    -> throws InvalidInputException
 *   2. Business logic      -> throws UserNotFoundException
 *   3. DB error wrapping   -> throws DataAccessException
 *
 * GlobalExceptionHandler catches all of these automatically.
 * The controller never needs a try-catch block.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * GET ALL USERS
     * Exception used: DataAccessException
     * - Wraps any DB failure during findAll()
     */
    public List<User> getAllUsers() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            // Wrap DB error into our custom exception -> caught by handleDatabaseError()
            throw new DataAccessException("Failed to fetch users from database", e);
        }
    }

    /**
     * GET USER BY ID
     * Exceptions used: InvalidInputException, UserNotFoundException, DataAccessException
     * - ID <= 0       -> InvalidInputException  -> 400
     * - ID not found  -> UserNotFoundException  -> 404
     * - DB fails      -> DataAccessException    -> 500
     */
    public User getUserById(Long id) {
        if (id == null || id <= 0) {
            // Input validation -> caught by handleInvalidInput()
            throw new InvalidInputException("User ID must be greater than zero");
        }
        try {
            return userRepository.findById(id)
                    // orElseThrow -> caught by handleUserNotFound()
                    .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        } catch (UserNotFoundException e) {
            throw e; // re-throw so GlobalExceptionHandler catches it correctly
        } catch (Exception e) {
            throw new DataAccessException("Database error while fetching user with id: " + id, e);
        }
    }

    /**
     * CREATE USER
     * Exceptions used: InvalidInputException, DataAccessException
     * - Blank name/email -> InvalidInputException -> 400
     * - DB save fails    -> DataAccessException   -> 500
     */
    public User createUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            throw new InvalidInputException("Name cannot be empty");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new InvalidInputException("Email cannot be empty");
        }
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            throw new DataAccessException("Database error while saving user", e);
        }
    }

    /**
     * UPDATE USER
     * Exceptions used: InvalidInputException, UserNotFoundException, DataAccessException
     * - ID <= 0          -> InvalidInputException -> 400
     * - User not found   -> UserNotFoundException  -> 404 (via getUserById)
     * - Blank name/email -> InvalidInputException -> 400
     * - DB save fails    -> DataAccessException   -> 500
     */
    public User updateUser(Long id, User updatedUser) {
        if (id == null || id <= 0) {
            throw new InvalidInputException("User ID must be greater than zero");
        }
        User existing = getUserById(id); // reuses getUserById - throws UserNotFoundException if missing
        if (updatedUser.getName() == null || updatedUser.getName().isBlank()) {
            throw new InvalidInputException("Name cannot be empty");
        }
        if (updatedUser.getEmail() == null || updatedUser.getEmail().isBlank()) {
            throw new InvalidInputException("Email cannot be empty");
        }
        existing.setName(updatedUser.getName());
        existing.setEmail(updatedUser.getEmail());
        try {
            return userRepository.save(existing);
        } catch (Exception e) {
            throw new DataAccessException("Database error while updating user with id: " + id, e);
        }
    }

    /**
     * DELETE USER
     * Exceptions used: InvalidInputException, UserNotFoundException, DataAccessException
     * - ID <= 0        -> InvalidInputException -> 400
     * - User not found -> UserNotFoundException  -> 404 (via getUserById)
     * - DB delete fails-> DataAccessException   -> 500
     */
    public void deleteUser(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidInputException("User ID must be greater than zero");
        }
        getUserById(id); // throws UserNotFoundException if user does not exist
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            throw new DataAccessException("Database error while deleting user with id: " + id, e);
        }
    }
}
