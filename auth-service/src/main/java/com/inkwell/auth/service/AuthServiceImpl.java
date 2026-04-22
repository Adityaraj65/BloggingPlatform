package com.inkwell.auth.service;

import com.inkwell.auth.entity.User;
import com.inkwell.auth.dto.RegisterRequest;
import com.inkwell.auth.repository.UserRepository;
import com.inkwell.auth.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public User register(RegisterRequest request) {
        // 1. Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Error: Email is already registered!");
        }

        // 2. Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Error: Username is already taken!");
        }

        // 3. If everything is fine, proceed with registration
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setFullName(request.getFullName());
        
        return userRepository.save(user);
    }

    @Override
    public String login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (passwordEncoder.matches(password, user.getPassword())) {
            return jwtUtil.generateToken(username);
        }
        throw new RuntimeException("Invalid Credentials");
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public User updateProfile(Long id, User updatedUser) {
        User user = getUserById(id);
        user.setFullName(updatedUser.getFullName());
        user.setBio(updatedUser.getBio());
        user.setAvatarUrl(updatedUser.getAvatarUrl());
        return userRepository.save(user);
    }

    @Override
    public void deactivateAccount(Long id) {
        User user = getUserById(id);
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public List<User> searchUsers(String query) {
        return userRepository.findByUsernameContainingIgnoreCase(query);
    }

    // Other methods like logout/refreshToken can be added as we implement OAuth/Redis
    @Override public void logout(String token) {}
    @Override public boolean validateToken(String token) { return jwtUtil.validateToken(token); }
    @Override public String refreshToken(String token) { return null; }
    @Override public User getUserByEmail(String email) { return userRepository.findByEmail(email).orElse(null); }
    @Override public void changePassword(Long id, String newPassword) {
        User user = getUserById(id);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}