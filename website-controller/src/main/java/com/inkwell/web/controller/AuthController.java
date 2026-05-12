package com.inkwell.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inkwell.web.client.AuthClient;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    private final AuthClient authClient;

    public AuthController(AuthClient authClient) {
        this.authClient = authClient;
    }

    @PostMapping("/login")
    @ResponseBody
    public Object login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session) {

        Map<String, String> req = new HashMap<>();

        req.put("email", email);
        req.put("password", password);

        Map<String, String> response = authClient.login(req);

        System.out.println("LOGIN RESPONSE = " + response);

        String token = response.get("token");

        System.out.println("TOKEN = " + token);

        session.setAttribute("JWT", token);

        return response;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:/login";
    }
}