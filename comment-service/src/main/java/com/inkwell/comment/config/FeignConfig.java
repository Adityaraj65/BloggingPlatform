package com.inkwell.comment.config;

import feign.RequestInterceptor;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {

        return requestTemplate -> {

            ServletRequestAttributes attributes =
                    (ServletRequestAttributes)
                    RequestContextHolder.getRequestAttributes();

            if (attributes == null) {
                return;
            }

            HttpServletRequest request =
                    attributes.getRequest();

            String authHeader =
                    request.getHeader("Authorization");

            String authUser =
                    request.getHeader("X-auth-user");

            String authRole =
                    request.getHeader("X-auth-role");

            if (authHeader != null) {

                requestTemplate.header(
                        "Authorization",
                        authHeader
                );
            }

            if (authUser != null) {

                requestTemplate.header(
                        "X-auth-user",
                        authUser
                );
            }

            if (authRole != null) {

                requestTemplate.header(
                        "X-auth-role",
                        authRole
                );
            }
        };
    }
}