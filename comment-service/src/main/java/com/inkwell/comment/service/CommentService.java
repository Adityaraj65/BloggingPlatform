package com.inkwell.comment.service;

import com.inkwell.comment.dto.*;
import java.util.List;

public interface CommentService {

    CommentResponseDTO addComment(CommentRequestDTO dto);

    List<CommentResponseDTO> getCommentsByPost(Long postId);

    List<CommentResponseDTO> getReplies(Long parentId);

    CommentResponseDTO updateComment(Long id, String content);

    void deleteComment(Long id);

    void approveComment(Long id);

    void rejectComment(Long id);

    void likeComment(Long id);

    void unlikeComment(Long id);

    int getCommentCount(Long postId);
}