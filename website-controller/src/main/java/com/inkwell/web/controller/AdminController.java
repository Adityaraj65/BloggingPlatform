package com.inkwell.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    // DASHBOARD
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return "admin/dashboard";
    }

    // USERS
    @GetMapping("/users")
    public String users() {
        return "admin/users";
    }

    // DELETE USER
    @PostMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        return "redirect:/admin/users";
    }

    // CHANGE ROLE
    @PostMapping("/user/role")
    public String changeRole() {
        return "redirect:/admin/users";
    }

    // POSTS
    @GetMapping("/posts")
    public String posts() {
        return "admin/posts";
    }

    // DELETE POST
    @PostMapping("/post/delete/{id}")
    public String deletePost(@PathVariable Long id) {
        return "redirect:/admin/posts";
    }

    // CATEGORY
    @GetMapping("/categories")
    public String categories() {
        return "admin/categories";
    }

    // CREATE CATEGORY
    @PostMapping("/category/create")
    public String createCategory() {
        return "redirect:/admin/categories";
    }

    // DELETE CATEGORY
    @PostMapping("/category/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        return "redirect:/admin/categories";
    }

    // NEWSLETTER
    @PostMapping("/newsletter/send")
    public String sendNewsletter() {
        return "redirect:/admin/dashboard";
    }

    // ANALYTICS
    @GetMapping("/analytics")
    public String analytics() {
        return "admin/analytics";
    }
}