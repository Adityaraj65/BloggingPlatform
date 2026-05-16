package com.inkwell.auth.controller;

import com.inkwell.auth.dto.*;
import com.inkwell.auth.service.AuthServiceImpl;
import com.inkwell.auth.util.JwtUtil;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthServiceImpl service;
    private final JwtUtil jwtUtil;

    public AuthController(AuthServiceImpl service, JwtUtil jwtUtil) {
        this.service = service;
        this.jwtUtil = jwtUtil;
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

    // OAuth2 Onboarding - Complete role selection
    @PostMapping("/oauth/complete-onboarding")
    public ResponseEntity<String> completeOAuth2Onboarding(@Valid @RequestBody OAuth2OnboardingRequest req) {
        return ResponseEntity.ok(service.completeOAuth2Onboarding(req.getTempToken(), req.getRole()));
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

    // Verify Email
    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        return ResponseEntity.ok(service.verifyEmail(token));
    }

    // Resend Verification
    @PostMapping("/resend-verification")
    public ResponseEntity<String> resendVerification(@RequestParam String email) {
        return ResponseEntity.ok(service.resendVerification(email));
    }

    // Forgot Password
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest req) {
        return ResponseEntity.ok(service.forgotPassword(req.getEmail()));
    }

    // Reset Password
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest req) {
        return ResponseEntity.ok(service.resetPassword(req.getToken(), req.getNewPassword()));
    }
}