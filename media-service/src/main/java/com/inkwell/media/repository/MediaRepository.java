package com.inkwell.media.repository;

import com.inkwell.media.entity.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MediaRepository extends JpaRepository<Media, Long> {
    // Find all media files linked to a specific blog post
    List<Media> findByLinkedPostId(Long postId);
    
    // Find only active (not soft-deleted) files
    List<Media> findByIsDeletedFalse();
}