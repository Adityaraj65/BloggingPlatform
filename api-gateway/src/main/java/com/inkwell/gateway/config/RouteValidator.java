package com.inkwell.gateway.config;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

// This class decides which APIs need authentication
@Component
public class RouteValidator {

    // Public endpoints (no token required)
    public static final List<String> openApiEndpoints = List.of(
            "/auth/register",
            "/auth/login",
            "/auth/refresh",
            "/eureka"
    );

    // Predicate checks if request is secured
    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().startsWith(uri));
}