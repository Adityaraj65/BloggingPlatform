package com.inkwell.post.service;

import com.inkwell.post.dto.PostRequestDTO;
import com.inkwell.post.dto.PostResponseDTO;
import java.util.List;

public interface PostService {
    // Standard CRUD
    PostResponseDTO createPost(PostRequestDTO dto);
    PostResponseDTO updatePost(Long id, PostRequestDTO dto);
    void deletePost(Long id);
    PostResponseDTO getPostById(Long id);
    PostResponseDTO getPostBySlug(String slug);

    // Business Logic / Moderation (From Figure 3)
    void publishPost(Long id);
    void unpublishPost(Long id); 
    void approvePost(Long id);  
    void rejectPost(Long id);    

    // Interactions
    void likePost(Long id);
    void unlikePost(Long id);   
    void incrementViews(Long id);

    // Queries
    List<PostResponseDTO> getPostsByAuthor(Long authorId);
    List<PostResponseDTO> getPublishedPosts();
    List<PostResponseDTO> searchPosts(String query);
    int getPostCountByAuthor(Long authorId); 
}