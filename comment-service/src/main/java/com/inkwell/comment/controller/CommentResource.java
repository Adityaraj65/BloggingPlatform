package com.inkwell.comment.controller;

import com.inkwell.comment.dto.*;
import com.inkwell.comment.service.CommentService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentResource {

    private final CommentService service;

    public CommentResource(CommentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CommentResponseDTO> add(@Valid @RequestBody CommentRequestDTO dto) {
        return ResponseEntity.ok(service.addComment(dto));
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentResponseDTO>> get(@PathVariable Long postId) {
        return ResponseEntity.ok(service.getCommentsByPost(postId));
    }

    @GetMapping("/replies/{id}")
    public ResponseEntity<List<CommentResponseDTO>> replies(@PathVariable Long id) {
        return ResponseEntity.ok(service.getReplies(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponseDTO> update(@PathVariable Long id,
                                                     @RequestParam String content) {
        return ResponseEntity.ok(service.updateComment(id, content));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteComment(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/approve/{id}")
    public ResponseEntity<Void> approve(@PathVariable Long id) {
        service.approveComment(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/reject/{id}")
    public ResponseEntity<Void> reject(@PathVariable Long id) {
        service.rejectComment(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/like/{id}")
    public ResponseEntity<Void> like(@PathVariable Long id) {
        service.likeComment(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/unlike/{id}")
    public ResponseEntity<Void> unlike(@PathVariable Long id) {
        service.unlikeComment(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/count/{postId}")
    public ResponseEntity<Integer> count(@PathVariable Long postId) {
        return ResponseEntity.ok(service.getCommentCount(postId));
    }
}