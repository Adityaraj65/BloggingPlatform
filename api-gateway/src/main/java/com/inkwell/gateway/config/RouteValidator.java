package com.inkwell.gateway.config;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class RouteValidator {

    // ================= PUBLIC ENDPOINTS =================
    // Anything matching these paths will NOT require JWT

    public static final List<String> openApiEndpoints = List.of(

            // AUTH
            "/auth/register",
            "/auth/login",
            "/auth/refresh",

            // EUREKA
            "/eureka",

            // CATEGORY
            "/categories/all",
            "/categories/id/",
            "/categories/slug/",
            "/categories/tags/",

            // TAGS
            "/tags",

            // POSTS
            "/posts/published",
            "/posts/slug/",

            // COMMENTS
            "/comments/post/",
            "/comments/replies/",

            // NEWSLETTER
            "/newsletter/subscribe",
            "/newsletter/confirm",

            // MEDIA PUBLIC FILES
            "/media/files/"
    );

    // ================= CHECK SECURED ROUTES =================

    public Predicate<ServerHttpRequest> isSecured =
            request -> {

                String path = request.getURI().getPath();

                return openApiEndpoints
                        .stream()
                        .noneMatch(path::startsWith);
            };
}