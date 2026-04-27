package com.inkwell.comment.service;

import com.inkwell.comment.dto.CommentRequestDTO;
import com.inkwell.comment.dto.CommentResponseDTO;
import com.inkwell.comment.entity.Comment;
import com.inkwell.comment.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public CommentResponseDTO addComment(CommentRequestDTO dto) {
        Comment comment = new Comment();
        comment.setPostId(dto.getPostId());
        comment.setAuthorId(dto.getAuthorId());
        comment.setContent(dto.getContent());
        comment.setParentCommentId(dto.getParentCommentId()); // Can be null if it's a main comment

        // Logic: New comments are PENDING by default until Admin approves
        comment.setStatus("PENDING");
        comment.setCreatedAt(LocalDateTime.now());
        comment.setLikesCount(0);

        // Logic: If it's a reply, verify if the parent comment exists
        if (dto.getParentCommentId() != null) {
            commentRepository.findById(dto.getParentCommentId())
                .orElseThrow(() -> new RuntimeException("Parent comment not found!"));
        }

        Comment savedComment = commentRepository.save(comment);
        return mapToDTO(savedComment);
    }

    @Override
    public List<CommentResponseDTO> getCommentsByPost(Long postId) {
        // Business Rule: Fetch only Top-level comments (where parent is null)
        return commentRepository.findByPostIdAndParentCommentIdIsNull(postId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentResponseDTO> getReplies(Long parentCommentId) {
        // Business Rule: Fetch all children for a specific parent
        return commentRepository.findByParentCommentId(parentCommentId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void approveComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        comment.setStatus("APPROVED");
        commentRepository.save(comment);
    }

    @Override
    public void likeComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        comment.setLikesCount(comment.getLikesCount() + 1);
        commentRepository.save(comment);
    }

    @Override
    public void unlikeComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        if (comment.getLikesCount() > 0) {
            comment.setLikesCount(comment.getLikesCount() - 1);
            commentRepository.save(comment);
        }
    }

    @Override
    public int getCommentCount(Long postId) {
        return commentRepository.countByPostId(postId);
    }

    // Helper: Map Entity to DTO
    private CommentResponseDTO mapToDTO(Comment comment) {
        CommentResponseDTO res = new CommentResponseDTO();
        res.setCommentId(comment.getCommentId());
        res.setPostId(comment.getPostId());
        res.setAuthorId(comment.getAuthorId());
        res.setContent(comment.getContent());
        res.setLikesCount(comment.getLikesCount());
        res.setStatus(comment.getStatus());
        res.setCreatedAt(comment.getCreatedAt());
        return res;
    }

    // Standard CRUD Placeholders
    @Override public void deleteComment(Long commentId) { commentRepository.deleteById(commentId); }
    @Override public void rejectComment(Long commentId) { /* set status REJECTED */ }
    @Override public CommentResponseDTO updateComment(Long commentId, String newContent) { /* update logic */ return null; }
}