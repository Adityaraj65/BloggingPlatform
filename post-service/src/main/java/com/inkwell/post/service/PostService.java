package com.inkwell.post.service;

import com.inkwell.post.dto.*;

import java.util.List;

public interface PostService {

    PostResponseDTO createPost(PostRequestDTO dto);

    PostResponseDTO getPostById(Long id);

    PostResponseDTO getPostBySlug(String slug);

    List<PostResponseDTO> getPostsByAuthor(Long authorId);

    List<PostResponseDTO> getPublishedPosts();

    List<PostResponseDTO> searchPosts(String query);

    PostResponseDTO updatePost(Long id, PostRequestDTO dto);

    void publishPost(Long id);

    void unpublishPost(Long id);

    void deletePost(Long id);

    void incrementViews(Long id);

    void likePost(Long id);

    void unlikePost(Long id);

    int getPostCountByAuthor(Long authorId);
}