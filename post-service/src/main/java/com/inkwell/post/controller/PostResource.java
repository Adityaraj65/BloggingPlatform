package com.inkwell.post.controller;
import jakarta.annotation.security.PermitAll;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inkwell.post.dto.PostRequestDTO;
import com.inkwell.post.dto.PostResponseDTO;
import com.inkwell.post.service.PostService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/posts")
public class PostResource {

    private final PostService service;

    public PostResource(PostService service) {
        this.service = service;
    }

    // ================= CREATE POST =================
    // AUTHOR or ADMIN
    @PreAuthorize("@roleSecurity.canCreateForAuthor(#dto.authorId)")
    @PostMapping
    public ResponseEntity<PostResponseDTO> create(
            @RequestBody PostRequestDTO dto,
            @RequestParam(defaultValue = "false") boolean publish) {

        return ResponseEntity.ok(
                service.createPost(dto, publish)
        );
    }

    // ================= GET POST BY ID =================
    @PermitAll
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> get(@PathVariable Long id) {

        return ResponseEntity.ok(service.getPostById(id));
    }

    // ================= GET POST BY SLUG =================
    @GetMapping("/slug/{slug}")
    public ResponseEntity<PostResponseDTO> getSlug(
            @PathVariable String slug) {

        return ResponseEntity.ok(service.getPostBySlug(slug));
    }

    // ================= GET PUBLISHED POSTS =================
    @GetMapping("/published")
    public ResponseEntity<List<PostResponseDTO>> published() {

        return ResponseEntity.ok(service.getPublishedPosts());
    }

    // ================= UPDATE POST =================
    // AUTHOR or ADMIN
    @PreAuthorize("@roleSecurity.canManagePost(#id)")
    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody PostRequestDTO dto) {

        return ResponseEntity.ok(service.updatePost(id, dto));
    }

    // ================= PUBLISH POST =================
    // OWNER AUTHOR or ADMIN
    @PreAuthorize("@roleSecurity.canManagePost(#id)")
    @PostMapping("/publish/{id}")
    public ResponseEntity<Void> publish(@PathVariable Long id) {

        service.publishPost(id);

        return ResponseEntity.ok().build();
    }

    // ================= UNPUBLISH POST =================
    // OWNER AUTHOR or ADMIN
    @PreAuthorize("@roleSecurity.canManagePost(#id)")
    @PostMapping("/unpublish/{id}")
    public ResponseEntity<Void> unpublish(@PathVariable Long id) {

        service.unpublishPost(id);

        return ResponseEntity.ok().build();
    }

    // ================= DELETE POST =================
    // OWNER AUTHOR or ADMIN
    @PreAuthorize("@roleSecurity.canManagePost(#id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        service.deletePost(id);

        return ResponseEntity.ok().build();
    }

    // ================= LIKE POST =================
    @PostMapping("/like/{id}")
    public ResponseEntity<Void> like(@PathVariable Long id) {

        service.likePost(id);

        return ResponseEntity.ok().build();
    }

    // ================= UNLIKE POST =================
    @PostMapping("/unlike/{id}")
    public ResponseEntity<Void> unlike(@PathVariable Long id) {

        service.unlikePost(id);

        return ResponseEntity.ok().build();
    }

    // ================= SEARCH POSTS =================
    @GetMapping("/search")
    public ResponseEntity<List<PostResponseDTO>> search(
            @RequestParam String query) {

        return ResponseEntity.ok(service.searchPosts(query));
    }

    // ================= GET POSTS BY AUTHOR =================
    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<PostResponseDTO>> getByAuthor(
            @PathVariable Long authorId) {

        return ResponseEntity.ok(service.getPostsByAuthor(authorId));
    }

    // ================= COUNT POSTS =================
    @GetMapping("/count/{authorId}")
    public ResponseEntity<Integer> count(
            @PathVariable Long authorId) {

        return ResponseEntity.ok(service.getPostCountByAuthor(authorId));
    }
}
