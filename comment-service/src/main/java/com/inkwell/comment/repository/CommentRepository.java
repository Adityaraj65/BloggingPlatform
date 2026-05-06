package com.inkwell.comment.repository;

import com.inkwell.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPostIdAndParentCommentIdIsNull(Long postId);

    List<Comment> findByParentCommentId(Long parentId);

    int countByPostId(Long postId);
}