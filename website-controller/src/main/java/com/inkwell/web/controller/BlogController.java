package com.inkwell.web.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.inkwell.web.client.PostClient;
import com.inkwell.web.dto.PostResponseDTO;

@Controller
public class BlogController {

    private final PostClient postClient;

    public BlogController(PostClient postClient) {
        this.postClient = postClient;
    }

    @GetMapping("/")
    public String home(Model model) {

        try {
            List<PostResponseDTO> posts = postClient.getPublished();
            model.addAttribute("posts", posts);

        } catch (Exception e) {

            // prevent full crash if microservice unavailable
            model.addAttribute("posts", Collections.emptyList());
            model.addAttribute("error", "Post service unavailable");
        }

        return "home";
    }

    @GetMapping("/post/{id}")
    public String viewPost(@PathVariable Long id, Model model) {

        try {
            model.addAttribute("post", postClient.getById(id));

        } catch (Exception e) {
            model.addAttribute("error", "Post not found");
        }

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