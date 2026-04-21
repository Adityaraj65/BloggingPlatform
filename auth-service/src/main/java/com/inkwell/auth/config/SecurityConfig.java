package com.inkwell.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Bcrypt hashing for passwords [cite: 733, 737]
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // Microservices me CSRF disable karte hain [cite: 733]
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll() // Registration/Login endpoints allow
                .anyRequest().authenticated()
            );
        return http.build();
    }
}