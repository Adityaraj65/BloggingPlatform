package com.inkwell.post.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.inkwell.post.client.CategoryClient;
import com.inkwell.post.dto.PostRequestDTO;
import com.inkwell.post.dto.PostResponseDTO;
import com.inkwell.post.entity.Post;
import com.inkwell.post.repository.PostRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository repo;
    private final CategoryClient categoryClient;
    private final HttpServletRequest request;

    public PostServiceImpl(
            PostRepository repo,
            CategoryClient categoryClient,
            HttpServletRequest request
    ) {
        this.repo = repo;
        this.categoryClient = categoryClient;
        this.request = request;
    }

    // ================= CREATE POST =================

    @Override
    public PostResponseDTO createPost(
            PostRequestDTO dto,
            boolean publish
    ) {

        if (dto.getAuthorId() == null) {

            throw new RuntimeException(
                    "Author required"
            );
        }

        Post post = new Post();

        post.setTitle(
                dto.getTitle() != null
                        ? dto.getTitle().trim()
                        : ""
        );

        post.setContent(
                dto.getContent() != null
                        ? dto.getContent().trim()
                        : ""
        );

        post.setExcerpt(
                dto.getExcerpt() != null
                        ? dto.getExcerpt().trim()
                        : ""
        );

        post.setFeaturedImageUrl(
                dto.getFeaturedImageUrl() != null
                        ? dto.getFeaturedImageUrl().trim()
                        : ""
        );

        post.setAuthorId(dto.getAuthorId());
        post.setAuthorName(dto.getAuthorName());

        post.setAuthorUsername(dto.getAuthorUsername());

        post.setAuthorAvatar(dto.getAuthorAvatar());

        post.setCategoryId(dto.getCategoryId());

        // ================= VALIDATE ONLY IF PUBLISHING =================

        if (publish) {

            if (post.getTitle().isBlank()) {
                throw new RuntimeException(
                        "Title required"
                );
            }

            if (post.getContent().isBlank()) {
                throw new RuntimeException(
                        "Content required"
                );
            }

            validateCategory(dto.getCategoryId());
        }

        // ================= SLUG =================

        String baseSlug =
                (post.getTitle().isBlank()
                        ? "draft-post"
                        : post.getTitle())
                .toLowerCase()
                .replaceAll("[^a-z0-9]", "-")
                .replaceAll("-+", "-");

        String slug = baseSlug;

        int counter = 1;

        while (repo.findBySlug(slug).isPresent()) {

            slug = baseSlug + "-" + counter++;
        }

        post.setSlug(slug);

        // ================= STATUS =================

        if (publish) {

            post.setStatus("PUBLISHED");

            post.setPublishedAt(
                    LocalDateTime.now()
            );

        } else {

            post.setStatus("DRAFT");
        }

        // ================= READ TIME =================

        int words = post.getContent()
                .trim()
                .isEmpty()
                ? 0
                : post.getContent()
                .trim()
                .split("\\s+").length;

        post.setReadTimeMin(
                Math.max(1, words / 180)
        );

        post.setCreatedAt(LocalDateTime.now());

        post.setUpdatedAt(LocalDateTime.now());

        return map(repo.save(post));
    }
    // ================= GET POST =================

    @Override
    public PostResponseDTO getPostById(Long id) {

        Post post = find(id);

        validateVisible(post);

        return map(post);
    }

    @Override
    public PostResponseDTO getPostBySlug(String slug) {

        Post post = repo.findBySlug(slug)
                .orElseThrow(() ->
                        new RuntimeException("Post not found"));

        validateVisible(post);

        post.setViewCount(
                post.getViewCount() + 1
        );

        repo.save(post);

        return map(post);
    }

    // ================= AUTHOR POSTS =================

    @Override
    public List<PostResponseDTO> getPostsByAuthor(Long authorId) {

        return repo
                .findByAuthorIdOrderByCreatedAtDesc(authorId)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    // ================= PUBLISHED FEED =================

    @Override
    public List<PostResponseDTO> getPublishedPosts() {

        return repo
                .findByStatusOrderByPublishedAtDesc("PUBLISHED")
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    // ================= SEARCH =================

    @Override
    public List<PostResponseDTO> searchPosts(String query) {

        return repo
                .findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
                        query,
                        query
                )
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    // ================= UPDATE =================

    @Override
    public PostResponseDTO updatePost(
            Long id,
            PostRequestDTO dto
    ) {

        Post post = find(id);

        post.setTitle(
                dto.getTitle() != null
                        ? dto.getTitle().trim()
                        : ""
        );

        post.setContent(
                dto.getContent() != null
                        ? dto.getContent().trim()
                        : ""
        );

        post.setExcerpt(
                dto.getExcerpt() != null
                        ? dto.getExcerpt().trim()
                        : ""
        );

        post.setFeaturedImageUrl(
                dto.getFeaturedImageUrl() != null
                        ? dto.getFeaturedImageUrl().trim()
                        : ""
        );

        post.setCategoryId(dto.getCategoryId());

        validateCategory(dto.getCategoryId());

        int words = post.getContent()
                .trim()
                .isEmpty()
                ? 0
                : post.getContent()
                .trim()
                .split("\\s+").length;

        post.setReadTimeMin(
                Math.max(1, words / 180)
        );

        post.setUpdatedAt(LocalDateTime.now());

        return map(repo.save(post));
    }

    // ================= PUBLISH =================

    @Override
    public void publishPost(Long id) {

        Post post = find(id);

        post.setStatus("PUBLISHED");

        post.setPublishedAt(LocalDateTime.now());

        repo.save(post);
    }

    // ================= UNPUBLISH =================

    @Override
    public void unpublishPost(Long id) {

        Post post = find(id);

        post.setStatus("UNPUBLISHED");

        post.setPublishedAt(null);

        repo.save(post);
    }

    // ================= DELETE =================

    @Override
    public void deletePost(Long id) {

        repo.deleteById(id);
    }

    // ================= VIEWS =================

    @Override
    public void incrementViews(Long id) {

        Post post = find(id);

        post.setViewCount(
                post.getViewCount() + 1
        );

        repo.save(post);
    }

    // ================= LIKES =================

    @Override
    public void likePost(Long id) {

        Post post = find(id);

        post.setLikesCount(
                post.getLikesCount() + 1
        );

        repo.save(post);
    }

    @Override
    public void unlikePost(Long id) {

        Post post = find(id);

        if (post.getLikesCount() > 0) {

            post.setLikesCount(
                    post.getLikesCount() - 1
            );

            repo.save(post);
        }
    }

    // ================= COUNT =================

    @Override
    public int getPostCountByAuthor(Long authorId) {

        return repo.countByAuthorId(authorId);
    }

    // ================= HELPERS =================

    private Post find(Long id) {

        return repo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Post not found"));
    }

    private PostResponseDTO map(Post p) {

        PostResponseDTO dto =
                new PostResponseDTO();

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
        dto.setAuthorId(p.getAuthorId());
        dto.setAuthorName(p.getAuthorName());

        dto.setAuthorUsername(p.getAuthorUsername());

        dto.setAuthorAvatar(p.getAuthorAvatar());

        return dto;
    }

    private void validateCategory(Long categoryId) {

        if (categoryId == null) {
            return;
        }

        try {

            categoryClient.getCategoryById(categoryId);

        } catch (Exception e) {

            throw new RuntimeException("Invalid category");
        }
    }

    private void validateVisible(Post post) {

        if ("PUBLISHED".equals(post.getStatus())) {
            return;
        }

        if (isAdmin()) {
            return;
        }

        Long currentUserId = currentUserId();

        if (
                currentUserId != null
                && currentUserId.equals(post.getAuthorId())
        ) {
            return;
        }

        throw new RuntimeException("Post not found");
    }

    private boolean isAdmin() {

        String role = request.getHeader("X-auth-role");

        return role != null
                && role.equalsIgnoreCase("ADMIN");
    }

    private Long currentUserId() {

        String userId = request.getHeader("X-auth-user-id");

        if (userId == null || userId.isBlank()) {
            return null;
        }

        try {
            return Long.parseLong(userId);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
