package com.inkwell.category.repository;

import com.inkwell.category.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    
    Optional<Tag> findBySlug(String slug);

    // Logic: Trending tags (Top tags by usage count)
    List<Tag> findTop10ByOrderByPostCountDesc();

    // Mapping logic (Simplified for now, will link to posts via a junction table if needed)
    // List<Tag> findByPostId(Long postId); 
}