package com.inkwell.post.repository;

import com.inkwell.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findBySlug(String slug);

    List<Post> findByAuthorId(Long authorId);

    List<Post> findByStatus(String status);

    // Ye wali line missing thi, ise add karo
    List<Post> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String titleQuery, String contentQuery);
}