package com.inkwell.auth.service;

import com.inkwell.auth.dto.*;
import com.inkwell.auth.entity.*;
import com.inkwell.auth.repository.UserRepository;
import com.inkwell.auth.util.JwtUtil;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthServiceImpl {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwt;

    public AuthServiceImpl(UserRepository repo, PasswordEncoder encoder, JwtUtil jwt) {
        this.repo = repo;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    // Register user
    public User register(RegisterRequest req) {

        if (repo.existsByEmail(req.getEmail()))
            throw new RuntimeException("Email exists");

        User u = new User();
        u.setUsername(req.getUsername());
        u.setEmail(req.getEmail());
        u.setPasswordHash(encoder.encode(req.getPassword()));
        u.setRole(Role.valueOf(req.getRole().toUpperCase()));

        return repo.save(u);
    }

    // Login user
    public String login(LoginRequest req) {

        User user = repo.findByUsername(req.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(req.getPassword(), user.getPasswordHash()))
            throw new RuntimeException("Invalid credentials");

        return jwt.generateToken(user.getUsername(), user.getRole().name());
    }

    public User getUser(Long id) {
        return repo.findByUserId(id).orElseThrow();
    }

    public List<User> search(String q) {
        return repo.findByUsernameContainingIgnoreCase(q);
    }
}