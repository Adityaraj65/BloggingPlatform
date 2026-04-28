package com.inkwell.post.controller;

import com.inkwell.post.dto.PostRequestDTO;
import com.inkwell.post.dto.PostResponseDTO;
import com.inkwell.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostResource {

    @Autowired
    private PostService postService;

    @PostMapping("/create")
    public ResponseEntity<PostResponseDTO> create(@RequestBody PostRequestDTO dto) {
        return ResponseEntity.ok(postService.createPost(dto));
    }
    @PostMapping("/add") 
    public ResponseEntity<PostResponseDTO> createPost(@RequestBody PostRequestDTO dto) {
        return ResponseEntity.ok(postService.createPost(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<PostResponseDTO> getBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(postService.getPostBySlug(slug));
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<PostResponseDTO>> getByAuthor(@PathVariable Long authorId) {
        return ResponseEntity.ok(postService.getPostsByAuthor(authorId));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<PostResponseDTO> update(@PathVariable Long id, @RequestBody PostRequestDTO dto) {
        return ResponseEntity.ok(postService.updatePost(id, dto));
    }

    @PostMapping("/publish/{id}")
    public ResponseEntity<Void> publish(@PathVariable Long id) {
        postService.publishPost(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<PostResponseDTO>> search(@RequestParam String query) {
        return ResponseEntity.ok(postService.searchPosts(query));
    }

    @PostMapping("/like/{id}")
    public ResponseEntity<Void> like(@PathVariable Long id) {
        postService.likePost(id);
        return ResponseEntity.ok().build();
    }
}