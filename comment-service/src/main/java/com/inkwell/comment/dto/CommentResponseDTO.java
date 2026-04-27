package com.inkwell.comment.dto;

import java.time.LocalDateTime;
import java.util.List;

public class CommentResponseDTO {
    private Long commentId;
    private Long postId;
    private Long authorId;
    private String content;
    private int likesCount;
    private String status;
    private LocalDateTime createdAt;
    
    // For threaded replies (Figure 4 requirement)
    private List<CommentResponseDTO> replies;

    // --- Getters and Setters ---
    public Long getCommentId() { return commentId; }
    public void setCommentId(Long commentId) { this.commentId = commentId; }

    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }

    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getLikesCount() { return likesCount; }
    public void setLikesCount(int likesCount) { this.likesCount = likesCount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<CommentResponseDTO> getReplies() { return replies; }
    public void setReplies(List<CommentResponseDTO> replies) { this.replies = replies; }
}