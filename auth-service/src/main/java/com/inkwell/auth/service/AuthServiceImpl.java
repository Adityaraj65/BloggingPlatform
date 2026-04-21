package com.inkwell.auth.service;

import com.inkwell.auth.entity.User;
import com.inkwell.auth.dto.LoginRequest;
import com.inkwell.auth.dto.RegisterRequest;
import com.inkwell.auth.repository.UserRepository;
import com.inkwell.auth.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; 

    public String register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return "Error: Email is already in use!"; 
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        user.setPassword(passwordEncoder.encode(request.getPassword())); 
        user.setRole(request.getRole()); 

        userRepository.save(user);
        return "User registered successfully!"; 
    }
    @Autowired
    private JwtUtil jwtUtil;

    public String login(LoginRequest request) {
        // 1. Fetch user from database
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found!"));

        // 2. Verify password using BCrypt
        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            // 3. Generate and return JWT Token
            return jwtUtil.generateToken(user.getUsername());
        } else {
            return "Error: Invalid credentials!";
        }
    }
}