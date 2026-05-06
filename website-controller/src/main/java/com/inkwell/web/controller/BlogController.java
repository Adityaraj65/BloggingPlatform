package com.inkwell.web.controller;

import com.inkwell.web.client.PostClient;
import com.inkwell.web.dto.PostResponseDTO;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class BlogController {

    private final PostClient postClient;

    public BlogController(PostClient postClient) {
        this.postClient = postClient;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<PostResponseDTO> posts = postClient.getPublished();
        model.addAttribute("posts", posts);
        return "home";
    }

    @GetMapping("/post/{id}")
    public String viewPost(@PathVariable Long id, Model model) {
        model.addAttribute("post", postClient.getById(id));
        return "post";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }
}