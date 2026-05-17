package com.inkwell.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/author")
public class AuthorController {

    // DASHBOARD
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return "author/dashboard";
    }

    // MY POSTS
    @GetMapping("/posts")
    public String myPosts(Model model) {
        return "author/posts";
    }

    // CREATE POST PAGE
    @GetMapping("/create")
    public String createPostPage() {
        return "author/create-post";
    }

    // SAVE POST
    @PostMapping("/save")
    public String savePost() {
        return "redirect:/author/posts";
    }

    // EDIT POST
    @GetMapping("/edit/{id}")
    public String editPost(@PathVariable Long id, Model model) {
        return "author/edit-post";
    }

    // UPDATE POST
    @PostMapping("/update/{id}")
    public String updatePost(@PathVariable Long id) {
        return "redirect:/author/posts";
    }

    // PUBLISH
    @PostMapping("/publish/{id}")
    public String publish(@PathVariable Long id) {
        return "redirect:/author/posts";
    }

    // DELETE POST
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        return "redirect:/author/posts";
    }

    // UPLOAD MEDIA
    @PostMapping("/media/upload")
    public String uploadMedia() {
        return "redirect:/author/dashboard";
    }

    // VIEW MEDIA
    @GetMapping("/media")
    public String media() {
        return "author/media";
    }

    // COMMENTS
    @GetMapping("/comments")
    public String comments() {
        return "author/comments";
    }
}