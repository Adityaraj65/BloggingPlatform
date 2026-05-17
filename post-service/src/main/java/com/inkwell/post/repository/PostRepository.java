package com.inkwell.post.repository;

import com.inkwell.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findBySlug(String slug);

    List<Post> findByAuthorId(Long authorId);

    List<Post> findByStatus(String status);

    List<Post> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String t, String c);

    List<Post> findByAuthorIdOrderByCreatedAtDesc(Long authorId);

    List<Post> findByStatusOrderByPublishedAtDesc(String status);

    int countByAuthorId(Long authorId);
}