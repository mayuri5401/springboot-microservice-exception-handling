package com.UserService.repository;

import com.UserService.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * USER REPOSITORY
 * ────────────────
 * CONCEPT: Spring Data JPA auto-generates all SQL queries (CRUD) at runtime.
 * No manual JDBC or SQL needed.
 *
 * JpaRepository<User, Long> provides:
 *   findAll()       -> SELECT * FROM users
 *   findById(id)    -> SELECT * FROM users WHERE id = ?
 *   save(user)      -> INSERT or UPDATE
 *   deleteById(id)  -> DELETE FROM users WHERE id = ?
 *
 * Any failure in these methods throws a Spring DataAccessException
 * which we catch in UserService and wrap into our custom DataAccessException.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
