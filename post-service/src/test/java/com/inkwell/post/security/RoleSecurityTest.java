package com.inkwell.post.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.inkwell.post.entity.Post;
import com.inkwell.post.repository.PostRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoleSecurityTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private PostRepository postRepository;

    private RoleSecurity roleSecurity;

    @BeforeEach
    void setUp() {
        roleSecurity = new RoleSecurity(request, postRepository);
    }

    @Test
    void hasRoleAndHasAnyRoleReadGatewayRoleHeaderCaseInsensitively() {
        // Arrange
        when(request.getHeader("X-auth-role")).thenReturn("admin");

        // Act + Assert
        assertThat(roleSecurity.hasRole("ADMIN")).isTrue();
        assertThat(roleSecurity.hasAnyRole("AUTHOR", "ADMIN")).isTrue();
        assertThat(roleSecurity.hasRole("READER")).isFalse();
    }

    @Test
    void canCreateForAuthorAllowsAdminOrMatchingAuthor() {
        // Arrange + Act + Assert
        when(request.getHeader("X-auth-role")).thenReturn("ADMIN");
        assertThat(roleSecurity.canCreateForAuthor(99L)).isTrue();

        when(request.getHeader("X-auth-role")).thenReturn("AUTHOR");
        when(request.getHeader("X-auth-user-id")).thenReturn("7");
        assertThat(roleSecurity.canCreateForAuthor(7L)).isTrue();
        assertThat(roleSecurity.canCreateForAuthor(8L)).isFalse();
    }

    @Test
    void canCreateForAuthorRejectsMissingAuthorOrInvalidUserHeader() {
        // Arrange
        when(request.getHeader("X-auth-role")).thenReturn("AUTHOR");
        when(request.getHeader("X-auth-user-id")).thenReturn("not-a-number");

        // Act + Assert
        assertThat(roleSecurity.canCreateForAuthor(7L)).isFalse();
        assertThat(roleSecurity.canCreateForAuthor(null)).isFalse();
    }

    @Test
    void canManagePostAllowsAdminWithoutRepositoryLookup() {
        // Arrange
        when(request.getHeader("X-auth-role")).thenReturn("ADMIN");

        // Act + Assert
        assertThat(roleSecurity.canManagePost(22L)).isTrue();
    }

    @Test
    void canManagePostAllowsOwningAuthorOnly() {
        // Arrange
        Post post = new Post();
        post.setAuthorId(7L);
        when(request.getHeader("X-auth-role")).thenReturn("AUTHOR");
        when(request.getHeader("X-auth-user-id")).thenReturn("7");
        when(postRepository.findById(22L)).thenReturn(Optional.of(post));

        // Act + Assert
        assertThat(roleSecurity.canManagePost(22L)).isTrue();

        post.setAuthorId(8L);
        assertThat(roleSecurity.canManagePost(22L)).isFalse();
    }

    @Test
    void canManagePostRejectsNullPostMissingUserAndMissingPost() {
        // Arrange
        when(request.getHeader("X-auth-role")).thenReturn("AUTHOR");

        // Act + Assert
        assertThat(roleSecurity.canManagePost(null)).isFalse();
        assertThat(roleSecurity.canManagePost(22L)).isFalse();

        when(request.getHeader("X-auth-user-id")).thenReturn("7");
        when(postRepository.findById(22L)).thenReturn(Optional.empty());
        assertThat(roleSecurity.canManagePost(22L)).isFalse();
    }
}
