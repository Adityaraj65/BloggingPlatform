package com.inkwell.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth

                    // PUBLIC
                    .requestMatchers(
                            "/",
                            "/login",
                            "/register",
                            "/css/**",
                            "/js/**",
                            "/images/**"
                    ).permitAll()

                    // AUTHOR
                    .requestMatchers("/author/**").permitAll()

                    // ADMIN
                    .requestMatchers("/admin/**").permitAll()

                    // EVERYTHING ELSE
                    .anyRequest().permitAll()
            )

            // DISABLE SPRING LOGIN SYSTEM
            .formLogin(form -> form.disable())

            .httpBasic(Customizer.withDefaults())

            .logout(logout -> logout.disable());

        return http.build();
    }
}