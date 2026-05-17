package com.inkwell.auth.dto;

import jakarta.validation.constraints.NotBlank;

public class OAuth2OnboardingRequest {

    @NotBlank(message = "Role is required")
    private String role;

    @NotBlank(message = "Temporary token is required")
    private String tempToken;

    public OAuth2OnboardingRequest() {}

    public OAuth2OnboardingRequest(String role, String tempToken) {
        this.role = role;
        this.tempToken = tempToken;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTempToken() {
        return tempToken;
    }

    public void setTempToken(String tempToken) {
        this.tempToken = tempToken;
    }
}
