package com.inkwell.web.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "AUTH-SERVICE")
public interface AuthClient {

    @PostMapping("/auth/login")
    Map<String, String> login(@RequestBody Map<String, String> request);

    @PostMapping("/auth/register")
    Object register(@RequestBody Map<String, String> request);

    @GetMapping("/auth/profile")
    Object getProfile(@RequestHeader("Authorization") String token);
}