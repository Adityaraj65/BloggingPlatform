package com.inkwell.auth.repository;

import com.inkwell.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    
    // For search functionality in diagram
    List<User> findByUsernameContainingIgnoreCase(String username);
    
    // For role-based filtering
    List<User> findAllByRole(String role);
    
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}