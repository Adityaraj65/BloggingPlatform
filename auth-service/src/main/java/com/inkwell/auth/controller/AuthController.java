package com.inkwell.auth.controller;

import com.inkwell.auth.dto.*;
import com.inkwell.auth.service.AuthServiceImpl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthServiceImpl service;

    public AuthController(AuthServiceImpl service) {
        this.service = service;
    }

    // Register API
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest req) {
        return ResponseEntity.ok(service.register(req));
    }

    // Login API
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(service.login(req));
    }

    // Get profile
    @GetMapping("/profile/{id}")
    public ResponseEntity<UserResponse> profile(@PathVariable Long id) {
        return ResponseEntity.ok(service.getUser(id));
    }

    // Update profile
    @PutMapping("/profile/{id}")
    public ResponseEntity<UserResponse> updateProfile(@PathVariable Long id, @Valid @RequestBody UpdateProfileRequest req) {
        return ResponseEntity.ok(service.updateProfile(id, req));
    }
}