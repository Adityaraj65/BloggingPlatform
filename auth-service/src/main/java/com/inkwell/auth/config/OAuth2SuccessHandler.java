package com.inkwell.auth.config;

import com.inkwell.auth.entity.Role;
import com.inkwell.auth.entity.User;
import com.inkwell.auth.repository.UserRepository;
import com.inkwell.auth.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        // Extract OAuth2 user details from Google
        DefaultOAuth2User oauth2User = (DefaultOAuth2User) authentication.getPrincipal();
        
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        String picture = oauth2User.getAttribute("picture");

        // Check if user already exists by email
        Optional<User> existingUser = userRepository.findByEmail(email);
        
        User user;
        boolean isNewUser = false;
        
        if (existingUser.isPresent()) {
            // User exists - link OAuth login with existing account
            user = existingUser.get();
            user.setProvider("GOOGLE");
            
            // Update profile if missing
            if (user.getFullName() == null || user.getFullName().isBlank()) {
                user.setFullName(name);
            }
            if (user.getAvatarUrl() == null || user.getAvatarUrl().isBlank()) {
                user.setAvatarUrl(picture);
            }
        } else {
            // New user - create account with OAuth2
            // Initially set to READER; user will select role during onboarding
            isNewUser = true;
            user = new User();
            user.setEmail(email);
            user.setUsername(extractUsernameFromEmail(email));
            user.setFullName(name);
            user.setAvatarUrl(picture);
            user.setRole(Role.READER); // Temporary role - will be updated during onboarding
            user.setProvider("GOOGLE");
            user.setIsActive(true);
            user.setEmailVerified(true);
            
            // Set a placeholder password (not used for OAuth users)
            user.setPassword(""); // Empty password for OAuth users
        }
        
        // Save user
        user = userRepository.save(user);
        
        // Generate JWT using existing token generation logic
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name(), user.getUserId());
        
        // Build redirect URL based on whether user is new
        String redirectUrl;
        if (isNewUser) {
            // New user: redirect to role selection with temporary token
            redirectUrl = "http://localhost:4200/oauth-role-selection?tempToken=" + token + "&isNewUser=true";
        } else {
            // Existing user: redirect to success page with final token
            redirectUrl = "http://localhost:4200/oauth-success?token=" + token + "&isNewUser=false";
        }
        
        response.sendRedirect(redirectUrl);
    }

    /**
     * Extract username from email (part before @)
     * If username exists, append a random suffix
     */
    private String extractUsernameFromEmail(String email) {
        String baseUsername = email.split("@")[0];
        String username = baseUsername;
        int counter = 1;

        // Check if username already exists, if so, make it unique
        while (userRepository.existsByUsername(username)) {
            username = baseUsername + counter;
            counter++;
        }

        return username;
    }
}
