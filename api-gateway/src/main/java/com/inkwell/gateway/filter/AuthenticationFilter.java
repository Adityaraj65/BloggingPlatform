package com.inkwell.gateway.filter;

import com.inkwell.gateway.config.RouteValidator;
import com.inkwell.gateway.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.*;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.http.server.reactive.ServerHttpRequest;

import reactor.core.publisher.Mono;

// This filter runs for secured routes
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    public static class Config {}

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {

            // Check if endpoint is secured
            if (validator.isSecured.test(exchange.getRequest())) {

                HttpHeaders headers = exchange.getRequest().getHeaders();

                // Check Authorization header
                if (!headers.containsKey(HttpHeaders.AUTHORIZATION)) {
                    return onError(exchange, "Missing Authorization Header", HttpStatus.UNAUTHORIZED);
                }

                String token = headers.getFirst(HttpHeaders.AUTHORIZATION);

                if (token != null && token.startsWith("Bearer ")) {
                    token = token.substring(7);
                }

                // Validate token
                if (!jwtUtil.validateToken(token)) {
                    return onError(exchange, "Invalid Token", HttpStatus.UNAUTHORIZED);
                }

                // Extract username
                String username = jwtUtil.extractUsername(token);

                // Forward username to downstream services
                ServerHttpRequest modifiedRequest = exchange.getRequest()
                        .mutate()
                        .header("X-auth-user", username)
                        .build();

                return chain.filter(exchange.mutate().request(modifiedRequest).build());
            }

            return chain.filter(exchange);
        };
    }

    // Common error handler
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus status) {

        exchange.getResponse().setStatusCode(status);

        byte[] bytes = err.getBytes();

        return exchange.getResponse()
                .writeWith(Mono.just(exchange.getResponse()
                        .bufferFactory()
                        .wrap(bytes)));
    }
}