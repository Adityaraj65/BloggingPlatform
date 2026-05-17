package com.inkwell.web.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "API-GATEWAY",
        contextId = "authClient"
)
public interface AuthClient {

    @PostMapping("/auth/login")
    Map<String, String> login(@RequestBody Map<String, String> request);

    @PostMapping("/auth/register")
    Object register(@RequestBody Map<String, String> request);

    @GetMapping("/auth/profile")
    Object getProfile(@RequestHeader("Authorization") String token);
}