package com.inkwell.post.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inkwell.post.dto.PostRequestDTO;
import com.inkwell.post.dto.PostResponseDTO;
import com.inkwell.post.exception.GlobalExceptionHandler;
import com.inkwell.post.security.RoleSecurity;
import com.inkwell.post.security.SecurityConfig;
import com.inkwell.post.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PostResource.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
class PostResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostService service;

    @MockBean(name = "roleSecurity")
    private RoleSecurity roleSecurity;

    @Test
    void getPostByIdReturnsResponseForPublicEndpoint() throws Exception {
        // Arrange
        when(service.getPostById(11L)).thenReturn(response(11L, "Published"));

        // Act + Assert
        mockMvc.perform(get("/posts/{id}", 11L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(11))
                .andExpect(jsonPath("$.title").value("Published"));
        verify(service).getPostById(11L);
    }

    @Test
    void publishedReturnsList() throws Exception {
        // Arrange
        when(service.getPublishedPosts()).thenReturn(List.of(response(1L, "One"), response(2L, "Two")));

        // Act + Assert
        mockMvc.perform(get("/posts/published"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].postId").value(1))
                .andExpect(jsonPath("$[1].title").value("Two"));
    }

    @Test
    void createAllowsAuthorWhenRoleSecurityApproves() throws Exception {
        // Arrange
        PostRequestDTO request = request(5L);
        when(roleSecurity.canCreateForAuthor(5L)).thenReturn(true);
        when(service.createPost(any(PostRequestDTO.class), eq(true))).thenReturn(response(99L, "Created"));

        // Act + Assert
        mockMvc.perform(post("/posts")
                        .header("X-auth-user", "author")
                        .header("X-auth-role", "AUTHOR")
                        .header("X-auth-user-id", "5")
                        .param("publish", "true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(99));
        verify(service).createPost(any(PostRequestDTO.class), eq(true));
    }

    @Test
    void createRejectsUnauthenticatedRequestBeforeServiceCall() throws Exception {
        // Arrange
        PostRequestDTO request = request(5L);

        // Act + Assert
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isForbidden());
        verify(service, never()).createPost(any(), eq(false));
    }

    @Test
    void createRejectsAuthenticatedUserWhenRoleSecurityDenies() throws Exception {
        // Arrange
        PostRequestDTO request = request(5L);
        when(roleSecurity.canCreateForAuthor(5L)).thenReturn(false);

        // Act + Assert
        mockMvc.perform(post("/posts")
                        .header("X-auth-user", "other-author")
                        .header("X-auth-role", "AUTHOR")
                        .header("X-auth-user-id", "9")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Access Denied"));
        verify(service, never()).createPost(any(), eq(false));
    }

    @Test
    void updateReturnsValidationErrorsForInvalidImageUrl() throws Exception {
        // Arrange
        PostRequestDTO request = request(5L);
        request.setFeaturedImageUrl("ftp://invalid");
        when(roleSecurity.canManagePost(77L)).thenReturn(true);

        // Act + Assert
        mockMvc.perform(put("/posts/{id}", 77L)
                        .header("X-auth-user", "author")
                        .header("X-auth-role", "AUTHOR")
                        .header("X-auth-user-id", "5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation failed"))
                .andExpect(jsonPath("$.fields.featuredImageUrl").value("Invalid image URL format"));
        verify(service, never()).updatePost(any(), any());
    }

    @Test
    void updateDelegatesWhenAuthorized() throws Exception {
        // Arrange
        PostRequestDTO request = request(5L);
        when(roleSecurity.canManagePost(77L)).thenReturn(true);
        when(service.updatePost(eq(77L), any(PostRequestDTO.class))).thenReturn(response(77L, "Updated"));

        // Act + Assert
        mockMvc.perform(put("/posts/{id}", 77L)
                        .header("X-auth-user", "author")
                        .header("X-auth-role", "AUTHOR")
                        .header("X-auth-user-id", "5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated"));
        verify(service).updatePost(eq(77L), any(PostRequestDTO.class));
    }

    @Test
    void deleteDelegatesWhenAuthorized() throws Exception {
        // Arrange
        when(roleSecurity.canManagePost(12L)).thenReturn(true);

        // Act + Assert
        mockMvc.perform(delete("/posts/{id}", 12L)
                        .header("X-auth-user", "admin")
                        .header("X-auth-role", "ADMIN")
                        .header("X-auth-user-id", "1")
                        .with(csrf()))
                .andExpect(status().isOk());
        verify(service).deletePost(12L);
    }

    @Test
    void likeAndUnlikePublicMutationsDelegate() throws Exception {
        // Act + Assert
        mockMvc.perform(post("/posts/like/{id}", 4L)
                        .header("X-auth-user", "reader")
                        .header("X-auth-role", "READER")
                        .with(csrf()))
                .andExpect(status().isOk());
        mockMvc.perform(post("/posts/unlike/{id}", 4L)
                        .header("X-auth-user", "reader")
                        .header("X-auth-role", "READER")
                        .with(csrf()))
                .andExpect(status().isOk());
        verify(service).likePost(4L);
        verify(service).unlikePost(4L);
    }

    @Test
    void searchDelegatesQueryParameter() throws Exception {
        // Arrange
        when(service.searchPosts("spring")).thenReturn(List.of(response(1L, "Spring")));

        // Act + Assert
        mockMvc.perform(get("/posts/search").param("query", "spring"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Spring"));
        verify(service).searchPosts("spring");
    }

    @Test
    void runtimeExceptionIsMappedToBadRequestBody() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Post not found")).when(service).getPostById(404L);

        // Act + Assert
        mockMvc.perform(get("/posts/{id}", 404L))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Post not found"));
    }

    @Test
    void databaseLengthMessageIsSanitized() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Data too long for column featured_image_url"))
                .when(service).getPostById(405L);

        // Act + Assert
        mockMvc.perform(get("/posts/{id}", 405L))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Image URL is too large"));
    }

    @Test
    void missingSearchQueryReturnsClientError() throws Exception {
        // Act + Assert
        mockMvc.perform(get("/posts/search"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal server error"));
        verify(service, never()).searchPosts(any());
    }

    private static PostRequestDTO request(Long authorId) {
        PostRequestDTO dto = new PostRequestDTO();
        dto.setTitle("Title");
        dto.setContent("Content");
        dto.setFeaturedImageUrl("https://cdn.example/post.png");
        dto.setAuthorId(authorId);
        dto.setCategoryId(2L);
        return dto;
    }

    private static PostResponseDTO response(Long id, String title) {
        PostResponseDTO dto = new PostResponseDTO();
        dto.setPostId(id);
        dto.setTitle(title);
        dto.setSlug("slug-" + id);
        dto.setStatus("PUBLISHED");
        dto.setAuthorId(5L);
        return dto;
    }
}
