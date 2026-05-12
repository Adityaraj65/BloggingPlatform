package com.inkwell.post.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Configuration
public class FeignConfig implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {

        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            String authHeader = attributes.getRequest().getHeader("Authorization");
            String userHeader = attributes.getRequest().getHeader("X-auth-user");
            String roleHeader = attributes.getRequest().getHeader("X-auth-role");

            if (authHeader != null) {
                template.header("Authorization", authHeader);
            }
            // Also forward gateway headers if present
            if (userHeader != null) {
                template.header("X-auth-user", userHeader);
            }
            if (roleHeader != null) {
                template.header("X-auth-role", roleHeader);
            }
        }
    }
}