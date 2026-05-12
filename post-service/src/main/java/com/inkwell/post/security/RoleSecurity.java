package com.inkwell.post.security;

import org.springframework.stereotype.Component;

import com.inkwell.post.repository.PostRepository;

import jakarta.servlet.http.HttpServletRequest;

@Component("roleSecurity")
public class RoleSecurity {

    private final HttpServletRequest request;

    private final PostRepository postRepository;

    public RoleSecurity(
            HttpServletRequest request,
            PostRepository postRepository
    ) {
        this.request = request;
        this.postRepository = postRepository;
    }

    public boolean hasRole(String role) {

        String userRole = request.getHeader("X-auth-role");

        if (userRole == null) {
            return false;
        }

        return userRole.equalsIgnoreCase(role);
    }

    public boolean hasAnyRole(String... roles) {

        String userRole = request.getHeader("X-auth-role");

        if (userRole == null) {
            return false;
        }

        for (String role : roles) {

            if (userRole.equalsIgnoreCase(role)) {
                return true;
            }
        }

        return false;
    }

    public boolean canCreateForAuthor(Long authorId) {

        if (hasRole("ADMIN")) {
            return true;
        }

        if (!hasRole("AUTHOR") || authorId == null) {
            return false;
        }

        Long currentUserId = currentUserId();

        return currentUserId != null
                && currentUserId.equals(authorId);
    }

    public boolean canManagePost(Long postId) {

        if (hasRole("ADMIN")) {
            return true;
        }

        if (!hasRole("AUTHOR") || postId == null) {
            return false;
        }

        Long currentUserId = currentUserId();

        if (currentUserId == null) {
            return false;
        }

        return postRepository
                .findById(postId)
                .map(post -> currentUserId.equals(post.getAuthorId()))
                .orElse(false);
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
