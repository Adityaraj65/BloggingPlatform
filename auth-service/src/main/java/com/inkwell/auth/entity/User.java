package com.inkwell.auth.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    // Store encrypted password
    @Column(nullable = false)
    private String passwordHash;

    private String fullName;

    // Role stored as ENUM (safe and controlled)
    @Enumerated(EnumType.STRING)
    private Role role;

    private String bio;
    private String avatarUrl;

    // LOCAL / GOOGLE / GITHUB
    private String provider = "LOCAL";

    private boolean isActive = true;

    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters and Setters
    public Long getUserId() { return userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}