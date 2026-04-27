package com.inkwell.comment.repository;

import com.inkwell.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Get all comments for a specific post
    List<Comment> findByPostId(Long postId);

    // Get only top-level comments (where parentCommentId is null)
    List<Comment> findByPostIdAndParentCommentIdIsNull(Long postId);

    // Get all replies for a specific comment
    List<Comment> findByParentCommentId(Long parentCommentId);

    // Get comments by status (for Admin moderation)
    List<Comment> findByStatus(String status);

    // Count comments for a post
    int countByPostId(Long postId);
}