package com.inkwell.comment.controller;

import com.inkwell.comment.dto.CommentRequestDTO;
import com.inkwell.comment.dto.CommentResponseDTO;
import com.inkwell.comment.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentResource {

    @Autowired
    private CommentService commentService;

    // 1. Add a new comment or reply
    @PostMapping("/add")
    public ResponseEntity<CommentResponseDTO> addComment(@RequestBody CommentRequestDTO dto) {
        return ResponseEntity.ok(commentService.addComment(dto));
    }

    // 2. Get all top-level comments for a post
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentResponseDTO>> getByPost(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPost(postId));
    }

    // 3. Get replies for a specific comment
    @GetMapping("/replies/{commentId}")
    public ResponseEntity<List<CommentResponseDTO>> getReplies(@PathVariable Long commentId) {
        return ResponseEntity.ok(commentService.getReplies(commentId));
    }

    // 4. Admin: Approve a comment
    @PutMapping("/approve/{commentId}")
    public ResponseEntity<String> approve(@PathVariable Long commentId) {
        commentService.approveComment(commentId);
        return ResponseEntity.ok("Comment approved successfully!");
    }

    // 5. Social: Like a comment
    @PutMapping("/like/{commentId}")
    public ResponseEntity<Void> like(@PathVariable Long commentId) {
        commentService.likeComment(commentId);
        return ResponseEntity.status(200).build();
    }

    // 6. Get total comment count for a post
    @GetMapping("/count/{postId}")
    public ResponseEntity<Integer> getCount(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentCount(postId));
    }

    // 7. Delete a comment
    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<Void> delete(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}