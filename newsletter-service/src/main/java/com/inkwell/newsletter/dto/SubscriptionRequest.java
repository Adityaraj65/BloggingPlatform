package com.inkwell.newsletter.dto;

import lombok.Data;

@Data
public class SubscriptionRequest {
    private String email;
    private String fullName;
    private String preferences;
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
	public String getPreferences() {
		return preferences;
	}
	public void setPreferences(String preferences) {
		this.preferences = preferences;
	}
}