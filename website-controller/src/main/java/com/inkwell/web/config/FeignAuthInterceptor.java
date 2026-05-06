package com.inkwell.web.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class FeignAuthInterceptor implements RequestInterceptor {

    private final HttpServletRequest request;

    public FeignAuthInterceptor(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public void apply(RequestTemplate template) {

        String token = (String) request.getSession().getAttribute("JWT");

        if (token != null) {
            template.header("Authorization", "Bearer " + token);
        }
    }
}