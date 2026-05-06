package com.inkwell.post.service;

import com.inkwell.post.client.CategoryClient;
import com.inkwell.post.dto.*;
import com.inkwell.post.entity.Post;
import com.inkwell.post.repository.PostRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository repo;
    private final CategoryClient categoryClient;

    public PostServiceImpl(PostRepository repo, CategoryClient categoryClient) {
        this.repo = repo;
        this.categoryClient = categoryClient;
    }

    // CREATE POST
    @Override
    public PostResponseDTO createPost(PostRequestDTO dto) {

        // Validate category using Feign client
        if (dto.getCategoryId() != null) {
            categoryClient.getCategoryById(dto.getCategoryId());
        }

        Post post = new Post();

        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setExcerpt(dto.getExcerpt());
        post.setFeaturedImageUrl(dto.getFeaturedImageUrl());
        post.setAuthorId(dto.getAuthorId());
        post.setCategoryId(dto.getCategoryId());

        // Generate slug
        String baseSlug = dto.getTitle().toLowerCase().replaceAll("[^a-z0-9]", "-");
        String slug = baseSlug;
        int i = 1;

        while (repo.findBySlug(slug).isPresent()) {
            slug = baseSlug + "-" + i++;
        }

        post.setSlug(slug);

        // Default status
        post.setStatus("DRAFT");

        // Calculate read time
        int words = dto.getContent().split("\\s+").length;
        post.setReadTimeMin(Math.max(1, words / 200));

        return map(repo.save(post));
    }

    @Override
    public PostResponseDTO getPostById(Long id) {
        return map(find(id));
    }

    @Override
    public PostResponseDTO getPostBySlug(String slug) {
        Post post = repo.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // Increment views
        post.setViewCount(post.getViewCount() + 1);
        repo.save(post);

        return map(post);
    }

    @Override
    public List<PostResponseDTO> getPostsByAuthor(Long authorId) {
        return repo.findByAuthorId(authorId)
                .stream().map(this::map).collect(Collectors.toList());
    }

    @Override
    public List<PostResponseDTO> getPublishedPosts() {
        return repo.findByStatus("PUBLISHED")
                .stream().map(this::map).collect(Collectors.toList());
    }

    @Override
    public List<PostResponseDTO> searchPosts(String query) {
        return repo.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query, query)
                .stream().map(this::map).collect(Collectors.toList());
    }

    @Override
    public PostResponseDTO updatePost(Long id, PostRequestDTO dto) {

        Post post = find(id);

        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setExcerpt(dto.getExcerpt());
        post.setUpdatedAt(LocalDateTime.now());

        return map(repo.save(post));
    }

    @Override
    public void publishPost(Long id) {
        Post post = find(id);
        post.setStatus("PUBLISHED");
        post.setPublishedAt(LocalDateTime.now());
        repo.save(post);
    }

    @Override
    public void unpublishPost(Long id) {
        Post post = find(id);
        post.setStatus("UNPUBLISHED");
        repo.save(post);
    }

    @Override
    public void deletePost(Long id) {
        repo.deleteById(id);
    }

    @Override
    public void incrementViews(Long id) {
        Post post = find(id);
        post.setViewCount(post.getViewCount() + 1);
        repo.save(post);
    }

    @Override
    public void likePost(Long id) {
        Post post = find(id);
        post.setLikesCount(post.getLikesCount() + 1);
        repo.save(post);
    }

    @Override
    public void unlikePost(Long id) {
        Post post = find(id);
        if (post.getLikesCount() > 0) {
            post.setLikesCount(post.getLikesCount() - 1);
            repo.save(post);
        }
    }

    @Override
    public int getPostCountByAuthor(Long authorId) {
        return repo.countByAuthorId(authorId);
    }

    // helper
    private Post find(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    // mapper
    private PostResponseDTO map(Post p) {
        PostResponseDTO dto = new PostResponseDTO();

        dto.setPostId(p.getPostId());
        dto.setTitle(p.getTitle());
        dto.setSlug(p.getSlug());
        dto.setContent(p.getContent());
        dto.setExcerpt(p.getExcerpt());
        dto.setFeaturedImageUrl(p.getFeaturedImageUrl());
        dto.setStatus(p.getStatus());
        dto.setViewCount(p.getViewCount());
        dto.setLikesCount(p.getLikesCount());
        dto.setReadTimeMin(p.getReadTimeMin());
        dto.setCreatedAt(p.getCreatedAt());
        dto.setPublishedAt(p.getPublishedAt());
        dto.setCategoryId(p.getCategoryId());

        return dto;
    }
}