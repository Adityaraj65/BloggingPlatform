package com.inkwell.notification.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignConfig implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            String authToken = attributes.getRequest().getHeader("Authorization");
            String userHeader = attributes.getRequest().getHeader("X-auth-user");
            String roleHeader = attributes.getRequest().getHeader("X-auth-role");
            
            if (authToken != null) {
                template.header("Authorization", authToken);
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