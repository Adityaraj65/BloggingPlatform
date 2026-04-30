package com.inkwell.newsletter.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SubscriberDTO {
    private Long subscriberId;
    private String email;
    private String fullName;
    private String status;
    private LocalDateTime subscribedAt;
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
	public String getPreferences() {
		return preferences;
	}
	public void setPreferences(String preferences) {
		this.preferences = preferences;
	}
}