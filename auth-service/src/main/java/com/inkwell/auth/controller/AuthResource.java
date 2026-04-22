package com.inkwell.auth.controller;

import com.inkwell.auth.entity.User;
import com.inkwell.auth.dto.RegisterRequest;
import com.inkwell.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthResource {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> loginData) {
        String token = authService.login(loginData.get("username"), loginData.get("password"));
        return ResponseEntity.ok(token);
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<User> getProfile(@PathVariable Long id) {
        return ResponseEntity.ok(authService.getUserById(id));
    }

    @PutMapping("/profile/{id}")
    public ResponseEntity<User> updateProfile(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(authService.updateProfile(id, user));
    }

    @PutMapping("/password/{id}")
    public ResponseEntity<String> changePassword(@PathVariable Long id, @RequestBody Map<String, String> data) {
        authService.changePassword(id, data.get("newPassword"));
        return ResponseEntity.ok("Password changed successfully");
    }

    @DeleteMapping("/deactivate/{id}")
    public ResponseEntity<String> deactivate(@PathVariable Long id) {
        authService.deactivateAccount(id);
        return ResponseEntity.ok("Account deactivated");
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> search(@RequestParam String query) {
        return ResponseEntity.ok(authService.searchUsers(query));
    }
}