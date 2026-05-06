package com.inkwell.web.controller;

import com.inkwell.web.client.AuthClient;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class AuthController {

    private final AuthClient authClient;

    public AuthController(AuthClient authClient) {
        this.authClient = authClient;
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session) {

        Map<String, String> req = new HashMap<>();
        req.put("email", email);
        req.put("password", password);

        Map<String, String> response = authClient.login(req);

        // Store JWT in session
        session.setAttribute("JWT", response.get("token"));

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}