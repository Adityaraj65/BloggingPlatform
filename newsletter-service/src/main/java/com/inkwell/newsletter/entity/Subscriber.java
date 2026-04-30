package com.inkwell.newsletter.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Subscriber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subscriberId;

    @Column(unique = true)
    private String email;
    
    private Long userId; // Nullable for guests
    private String fullName;
    private String status; // PENDING, ACTIVE, UNSUBSCRIBED
    private LocalDateTime subscribedAt = LocalDateTime.now();
    private LocalDateTime unsubscribedAt;
    private String token; // For confirmation
    private String preferences;
	public Long getSubscriberId() {
		return subscriberId;
	}
	public void setSubscriberId(Long subscriberId) {
		this.subscriberId = subscriberId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public LocalDateTime getSubscribedAt() {
		return subscribedAt;
	}
	public void setSubscribedAt(LocalDateTime subscribedAt) {
		this.subscribedAt = subscribedAt;
	}
	public LocalDateTime getUnsubscribedAt() {
		return unsubscribedAt;
	}
	public void setUnsubscribedAt(LocalDateTime unsubscribedAt) {
		this.unsubscribedAt = unsubscribedAt;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getPreferences() {
		return preferences;
	}
	public void setPreferences(String preferences) {
		this.preferences = preferences;
	}
}