package com.inkwell.post.controller;

import com.inkwell.post.dto.*;
import com.inkwell.post.service.PostService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostResource {

    private final PostService service;

    public PostResource(PostService service) {
        this.service = service;
    }

    // ================= CREATE POST =================
    // Only AUTHOR or ADMIN can create posts
    @PreAuthorize("hasAnyRole('AUTHOR','ADMIN')")
    @PostMapping
    public ResponseEntity<PostResponseDTO> create(@Valid @RequestBody PostRequestDTO dto) {
        return ResponseEntity.ok(service.createPost(dto));
    }

    // ================= GET POST BY ID =================
    // Any authenticated user can access
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.getPostById(id));
    }

    // ================= GET POST BY SLUG =================
    // Public endpoint (used by frontend)
    @GetMapping("/slug/{slug}")
    public ResponseEntity<PostResponseDTO> getSlug(@PathVariable String slug) {
        return ResponseEntity.ok(service.getPostBySlug(slug));
    }

    // ================= GET PUBLISHED POSTS =================
    // Public feed (homepage)
    @GetMapping("/published")
    public ResponseEntity<List<PostResponseDTO>> published() {
        return ResponseEntity.ok(service.getPublishedPosts());
    }

    // ================= UPDATE POST =================
    // Only AUTHOR or ADMIN
    @PreAuthorize("hasAnyRole('AUTHOR','ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDTO> update(@PathVariable Long id,
                                                 @Valid @RequestBody PostRequestDTO dto) {
        return ResponseEntity.ok(service.updatePost(id, dto));
    }

    // ================= PUBLISH POST =================
    // Only ADMIN can publish
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/publish/{id}")
    public ResponseEntity<Void> publish(@PathVariable Long id) {
        service.publishPost(id);
        return ResponseEntity.ok().build();
    }

    // ================= UNPUBLISH POST =================
    // Only ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/unpublish/{id}")
    public ResponseEntity<Void> unpublish(@PathVariable Long id) {
        service.unpublishPost(id);
        return ResponseEntity.ok().build();
    }

    // ================= DELETE POST =================
    // Only ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deletePost(id);
        return ResponseEntity.ok().build();
    }

    // ================= LIKE POST =================
    // Any logged-in user
    @PostMapping("/like/{id}")
    public ResponseEntity<Void> like(@PathVariable Long id) {
        service.likePost(id);
        return ResponseEntity.ok().build();
    }

    // ================= UNLIKE POST =================
    // Any logged-in user
    @PostMapping("/unlike/{id}")
    public ResponseEntity<Void> unlike(@PathVariable Long id) {
        service.unlikePost(id);
        return ResponseEntity.ok().build();
    }

    // ================= SEARCH POSTS =================
    // Public search
    @GetMapping("/search")
    public ResponseEntity<List<PostResponseDTO>> search(@RequestParam String query) {
        return ResponseEntity.ok(service.searchPosts(query));
    }

    // ================= GET POSTS BY AUTHOR =================
    // Dashboard use-case
    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<PostResponseDTO>> getByAuthor(@PathVariable Long authorId) {
        return ResponseEntity.ok(service.getPostsByAuthor(authorId));
    }

    // ================= COUNT POSTS =================
    // Used for analytics/dashboard
    @GetMapping("/count/{authorId}")
    public ResponseEntity<Integer> count(@PathVariable Long authorId) {
        return ResponseEntity.ok(service.getPostCountByAuthor(authorId));
    }
}