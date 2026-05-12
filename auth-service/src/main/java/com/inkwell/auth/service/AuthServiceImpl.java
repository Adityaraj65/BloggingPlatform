package com.inkwell.auth.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.inkwell.auth.dto.LoginRequest;
import com.inkwell.auth.dto.RegisterRequest;
import com.inkwell.auth.dto.UserResponse;
import com.inkwell.auth.entity.Role;
import com.inkwell.auth.entity.User;
import com.inkwell.auth.repository.UserRepository;
import com.inkwell.auth.util.JwtUtil;

import com.inkwell.auth.dto.UpdateProfileRequest;

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

    // ============================
    // REGISTER USER
    // ============================
    public UserResponse register(RegisterRequest req) {

        // check duplicate email
        if (repo.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());

        //  encode password correctly
        user.setPassword(encoder.encode(req.getPassword()));

        // safe role handling (no crash)
        Role role = (req.getRole() == null || req.getRole().isBlank())
                ? Role.READER
                : Role.valueOf(req.getRole().toUpperCase());

        user.setRole(role);

        // optional fields
        user.setFullName(req.getFullName());
        user.setIsActive(true);

        User savedUser = repo.save(user);

        // map to DTO
        return mapToResponse(savedUser);
    }

    // ============================
    // LOGIN USER
    // ============================
    public String login(LoginRequest req) {

        User user = repo.findByUsername(req.getIdentifier())
                .orElseGet(() -> repo.findByEmail(req.getIdentifier())
                        .orElseThrow(() -> new RuntimeException("User not found")));

        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwt.generateToken(user.getUsername(), user.getRole().name(), user.getUserId());
    }

    // ============================
    // GET USER
    // ============================
    public UserResponse getUser(Long id) {
        User user = repo.findByUserId(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return mapToResponse(user);
    }

    // ============================
    // UPDATE PROFILE
    // ============================
    public UserResponse updateProfile(Long id, UpdateProfileRequest req) {
        User user = repo.findByUserId(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (req.getEmail() != null && !req.getEmail().isBlank() && !req.getEmail().equals(user.getEmail())) {
            if (repo.existsByEmail(req.getEmail())) {
                throw new RuntimeException("Email already exists");
            }
            user.setEmail(req.getEmail());
        }

        if (req.getFullName() != null && !req.getFullName().isBlank()) {
            user.setFullName(req.getFullName());
        }

        if (req.getPassword() != null && !req.getPassword().isBlank()) {
            user.setPassword(encoder.encode(req.getPassword()));
        }

        User updatedUser = repo.save(user);
        return mapToResponse(updatedUser);
    }

    // ============================
    // SEARCH USERS
    // ============================
    public List<User> search(String q) {
        return repo.findByUsernameContainingIgnoreCase(q);
    }

    // ============================
    // MAPPER METHOD (CLEAN)
    // ============================
    private UserResponse mapToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getUserId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().name());
        return response;
    }
}