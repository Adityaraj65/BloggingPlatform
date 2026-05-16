package com.inkwell.auth.service;

import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;

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
    private final EmailService emailService;

    public AuthServiceImpl(UserRepository repo, PasswordEncoder encoder, JwtUtil jwt, EmailService emailService) {
        this.repo = repo;
        this.encoder = encoder;
        this.jwt = jwt;
        this.emailService = emailService;
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
        user.setEmailVerified(false);
        user.setVerificationToken(UUID.randomUUID().toString());
        user.setVerificationTokenExpiry(LocalDateTime.now().plusHours(24));

        User savedUser = repo.save(user);
        
        try {
            emailService.sendVerificationEmail(savedUser.getEmail(), savedUser.getVerificationToken());
        } catch (Exception e) {
            System.err.println("Failed to send verification email: " + e.getMessage());
        }

        // map to DTO
        return mapToResponse(savedUser);
    }

    // ============================
    // LOGIN USER
    // ============================
    public String login(LoginRequest req) {

        User user = repo.findByUsernameOrEmail(req.getIdentifier(), req.getIdentifier())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        if ("LOCAL".equals(user.getProvider())) {
            boolean isOldUser = user.getEmailVerified() == null || (Boolean.FALSE.equals(user.getEmailVerified()) && user.getVerificationToken() == null);
            if (!isOldUser && !Boolean.TRUE.equals(user.getEmailVerified())) {
                throw new RuntimeException("Please verify your email before logging in.");
            }
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
    // COMPLETE OAUTH2 ONBOARDING
    // ============================
    public String completeOAuth2Onboarding(String tempToken, String selectedRole) {
        
        // Validate the temporary token
        if (!jwt.validateToken(tempToken)) {
            throw new RuntimeException("Invalid or expired temporary token");
        }

        // Extract username from token
        String username = jwt.extractUsername(tempToken);

        // Find user by username
        User user = repo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Parse and validate selected role
        Role role;
        try {
            role = Role.valueOf(selectedRole.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role: " + selectedRole);
        }

        // Update user role
        user.setRole(role);
        repo.save(user);

        // Generate final JWT with updated role
        return jwt.generateToken(user.getUsername(), role.name(), user.getUserId());
    }

    // ============================
    // EMAIL VERIFICATION
    // ============================
    public String verifyEmail(String token) {
        User user = repo.findByVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (user.getVerificationTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        user.setEmailVerified(true);
        user.setVerificationToken(null);
        user.setVerificationTokenExpiry(null);
        repo.save(user);

        return "Email verified successfully";
    }

    public String resendVerification(String email) {
        User user = repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (Boolean.TRUE.equals(user.getEmailVerified())) {
            throw new RuntimeException("Email already verified");
        }

        user.setVerificationToken(UUID.randomUUID().toString());
        user.setVerificationTokenExpiry(LocalDateTime.now().plusHours(24));
        repo.save(user);

        try {
            emailService.sendVerificationEmail(user.getEmail(), user.getVerificationToken());
        } catch (Exception e) {
            System.err.println("Failed to resend verification email: " + e.getMessage());
        }

        return "Verification email resent";
    }

    // ============================
    // FORGOT / RESET PASSWORD
    // ============================
    public String forgotPassword(String email) {
        java.util.Optional<User> optionalUser = repo.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setResetToken(UUID.randomUUID().toString());
            user.setResetTokenExpiry(LocalDateTime.now().plusHours(1));
            repo.save(user);

            try {
                emailService.sendPasswordResetEmail(user.getEmail(), user.getResetToken());
            } catch (Exception e) {
                System.err.println("Failed to send reset email: " + e.getMessage());
            }
        }
        // Always return success to prevent email enumeration
        return "If an account with that email exists, a password reset link has been sent.";
    }

    public String resetPassword(String token, String newPassword) {
        User user = repo.findByResetToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        user.setPassword(encoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        repo.save(user);

        return "Password reset successfully";
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