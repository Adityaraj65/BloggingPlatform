package com.inkwell.auth.service;

import com.inkwell.auth.entity.User;
import com.inkwell.auth.dto.RegisterRequest;
import java.util.List;

public interface AuthService {
    User register(RegisterRequest request);
    String login(String username, String password);
    void logout(String token);
    boolean validateToken(String token);
    String refreshToken(String token);
    User getUserByEmail(String email);
    User getUserById(Long id);
    User updateProfile(Long id, User user);
    void changePassword(Long id, String newPassword);
    List<User> searchUsers(String query);
    void deactivateAccount(Long id);
}