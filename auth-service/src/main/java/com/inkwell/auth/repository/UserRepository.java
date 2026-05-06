package com.inkwell.auth.repository;

import com.inkwell.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByUserId(Long id);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    List<User> findByUsernameContainingIgnoreCase(String query);
}