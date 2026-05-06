package com.inkwell.comment.service;

import com.inkwell.comment.client.PostClient;
import com.inkwell.comment.dto.*;
import com.inkwell.comment.entity.Comment;
import com.inkwell.comment.repository.CommentRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository repo;
    private final PostClient postClient;

    public CommentServiceImpl(CommentRepository repo, PostClient postClient) {
        this.repo = repo;
        this.postClient = postClient;
    }

    @Override
    public CommentResponseDTO addComment(CommentRequestDTO dto) {

        // Validate post exists (Feign call)
        postClient.getPost(dto.getPostId());

        Comment c = new Comment();
        c.setPostId(dto.getPostId());
        c.setAuthorId(dto.getAuthorId());
        c.setParentCommentId(dto.getParentCommentId());
        c.setContent(dto.getContent());
        c.setStatus("PENDING");
        c.setCreatedAt(LocalDateTime.now());

        return map(repo.save(c));
    }

    @Override
    public List<CommentResponseDTO> getCommentsByPost(Long postId) {
        return repo.findByPostIdAndParentCommentIdIsNull(postId)
                .stream()
                .map(c -> {
                    CommentResponseDTO dto = map(c);
                    dto.setReplies(getReplies(c.getCommentId()));
                    return dto;
                }).collect(Collectors.toList());
    }

    @Override
    public List<CommentResponseDTO> getReplies(Long parentId) {
        return repo.findByParentCommentId(parentId)
                .stream().map(this::map).collect(Collectors.toList());
    }

    @Override
    public CommentResponseDTO updateComment(Long id, String content) {
        Comment c = find(id);
        c.setContent(content);
        c.setUpdatedAt(LocalDateTime.now());
        return map(repo.save(c));
    }

    @Override
    public void deleteComment(Long id) {
        Comment c = find(id);
        c.setStatus("DELETED");
        repo.save(c);
    }

    @Override
    public void approveComment(Long id) {
        Comment c = find(id);
        c.setStatus("APPROVED");
        repo.save(c);
    }

    @Override
    public void rejectComment(Long id) {
        Comment c = find(id);
        c.setStatus("REJECTED");
        repo.save(c);
    }

    @Override
    public void likeComment(Long id) {
        Comment c = find(id);
        c.setLikesCount(c.getLikesCount() + 1);
        repo.save(c);
    }

    @Override
    public void unlikeComment(Long id) {
        Comment c = find(id);
        if (c.getLikesCount() > 0) {
            c.setLikesCount(c.getLikesCount() - 1);
            repo.save(c);
        }
    }

    @Override
    public int getCommentCount(Long postId) {
        return repo.countByPostId(postId);
    }

    private Comment find(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));
    }

    private CommentResponseDTO map(Comment c) {
        CommentResponseDTO dto = new CommentResponseDTO();
        dto.setCommentId(c.getCommentId());
        dto.setPostId(c.getPostId());
        dto.setAuthorId(c.getAuthorId());
        dto.setContent(c.getContent());
        dto.setLikesCount(c.getLikesCount());
        dto.setStatus(c.getStatus());
        dto.setCreatedAt(c.getCreatedAt());
        return dto;
    }
}