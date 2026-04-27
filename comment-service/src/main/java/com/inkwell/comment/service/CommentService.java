package com.inkwell.comment.service;

import com.inkwell.comment.dto.CommentRequestDTO;
import com.inkwell.comment.dto.CommentResponseDTO;
import java.util.List;

public interface CommentService {
    // Creating & Modifying
    CommentResponseDTO addComment(CommentRequestDTO dto);
    CommentResponseDTO updateComment(Long commentId, String newContent);
    void deleteComment(Long commentId);

    // Moderation (Admin)
    void approveComment(Long commentId);
    void rejectComment(Long commentId);

    // Fetching Logic
    List<CommentResponseDTO> getCommentsByPost(Long postId);
    List<CommentResponseDTO> getReplies(Long parentCommentId);
    
    // Social
    void likeComment(Long commentId);
    void unlikeComment(Long commentId);
    
    int getCommentCount(Long postId);
}