package com.inkwell.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.inkwell.post.client.CategoryClient;
import com.inkwell.post.dto.CategoryDTO;
import com.inkwell.post.dto.PostRequestDTO;
import com.inkwell.post.dto.PostResponseDTO;
import com.inkwell.post.entity.Post;
import com.inkwell.post.repository.PostRepository;

import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock
    private PostRepository repo;

    @Mock
    private CategoryClient categoryClient;

    @Mock
    private HttpServletRequest request;

    private PostServiceImpl service;

//    @BeforeEach
//    void setUp() {
//        service = new PostServiceImpl(repo, categoryClient, request);
//    }

    @Test
    void createPostSavesDraftWithTrimmedFieldsAndDefaultSlug() {
        // Arrange
        PostRequestDTO dto = requestDto("  My First Post!  ", "  body text  ", 42L, null);
        when(repo.findBySlug("my-first-post-")).thenReturn(Optional.empty());
        when(repo.save(any(Post.class))).thenAnswer(invocation -> {
            Post post = invocation.getArgument(0);
            post.setPostId(10L);
            return post;
        });

        // Act
        PostResponseDTO response = service.createPost(dto, false);

        // Assert
        assertThat(response.getPostId()).isEqualTo(10L);
        assertThat(response.getTitle()).isEqualTo("My First Post!");
        assertThat(response.getContent()).isEqualTo("body text");
        assertThat(response.getSlug()).isEqualTo("my-first-post-");
        assertThat(response.getStatus()).isEqualTo("DRAFT");
        assertThat(response.getReadTimeMin()).isEqualTo(1);
        assertThat(response.getPublishedAt()).isNull();
        verify(categoryClient, never()).getCategoryById(any());
    }

    @Test
    void createPostPublishesWithCategoryValidationAndUniqueSlug() {
        // Arrange
        PostRequestDTO dto = requestDto("Hello World", "publishable content", 7L, 3L);
        when(categoryClient.getCategoryById(3L)).thenReturn(new CategoryDTO());
        when(repo.findBySlug("hello-world")).thenReturn(Optional.of(post(99L, 7L, "PUBLISHED")));
        when(repo.findBySlug("hello-world-1")).thenReturn(Optional.empty());
        when(repo.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        PostResponseDTO response = service.createPost(dto, true);

        // Assert
        assertThat(response.getStatus()).isEqualTo("PUBLISHED");
        assertThat(response.getSlug()).isEqualTo("hello-world-1");
        assertThat(response.getPublishedAt()).isNotNull();
        verify(categoryClient).getCategoryById(3L);
    }

    @Test
    void createPostRejectsMissingAuthorBeforeSaving() {
        // Arrange
        PostRequestDTO dto = requestDto("Title", "Content", null, null);

        // Act + Assert
        assertThatThrownBy(() -> service.createPost(dto, false))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Author required");
        verify(repo, never()).save(any());
    }

    @Test
    void createPostRejectsPublishedPostWithoutContent() {
        // Arrange
        PostRequestDTO dto = requestDto("Title", "   ", 5L, null);

        // Act + Assert
        assertThatThrownBy(() -> service.createPost(dto, true))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Content required");
        verify(repo, never()).save(any());
    }

    @Test
    void createPostWrapsCategoryClientFailureAsInvalidCategory() {
        // Arrange
        PostRequestDTO dto = requestDto("Title", "Content", 5L, 9L);
        when(categoryClient.getCategoryById(9L)).thenThrow(new RuntimeException("downstream timeout"));

        // Act + Assert
        assertThatThrownBy(() -> service.createPost(dto, true))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Invalid category");
        verify(repo, never()).save(any());
    }

    @Test
    void getPostByIdReturnsPublishedPostForAnonymousRequest() {
        // Arrange
        Post post = post(1L, 11L, "PUBLISHED");
        when(repo.findById(1L)).thenReturn(Optional.of(post));

        // Act
        PostResponseDTO response = service.getPostById(1L);

        // Assert
        assertThat(response.getPostId()).isEqualTo(1L);
        assertThat(response.getStatus()).isEqualTo("PUBLISHED");
    }

    @Test
    void getPostByIdAllowsDraftForOwningAuthorHeader() {
        // Arrange
        Post post = post(2L, 77L, "DRAFT");
        when(repo.findById(2L)).thenReturn(Optional.of(post));
        when(request.getHeader("X-auth-role")).thenReturn("AUTHOR");
        when(request.getHeader("X-auth-user-id")).thenReturn("77");

        // Act
        PostResponseDTO response = service.getPostById(2L);

        // Assert
        assertThat(response.getPostId()).isEqualTo(2L);
        assertThat(response.getStatus()).isEqualTo("DRAFT");
    }

    @Test
    void getPostByIdHidesDraftFromDifferentUser() {
        // Arrange
        Post post = post(2L, 77L, "DRAFT");
        when(repo.findById(2L)).thenReturn(Optional.of(post));
        when(request.getHeader("X-auth-role")).thenReturn("AUTHOR");
        when(request.getHeader("X-auth-user-id")).thenReturn("bad-id");

        // Act + Assert
        assertThatThrownBy(() -> service.getPostById(2L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Post not found");
    }

    @Test
    void getPostBySlugIncrementsViewsAfterVisibilityCheck() {
        // Arrange
        Post post = post(3L, 12L, "PUBLISHED");
        post.setSlug("slug");
        post.setViewCount(4);
        when(repo.findBySlug("slug")).thenReturn(Optional.of(post));
        when(repo.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        PostResponseDTO response = service.getPostBySlug("slug");

        // Assert
        assertThat(response.getViewCount()).isEqualTo(5);
        ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);
        verify(repo).save(captor.capture());
        assertThat(captor.getValue().getViewCount()).isEqualTo(5);
    }

    @Test
    void updatePostValidatesCategoryAndSavesTrimmedContent() {
        // Arrange
        Post existing = post(4L, 8L, "DRAFT");
        PostRequestDTO dto = requestDto("  Updated  ", "  New content here  ", 8L, 4L);
        when(repo.findById(4L)).thenReturn(Optional.of(existing));
        when(categoryClient.getCategoryById(4L)).thenReturn(new CategoryDTO());
        when(repo.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        PostResponseDTO response = service.updatePost(4L, dto);

        // Assert
        assertThat(response.getTitle()).isEqualTo("Updated");
        assertThat(response.getContent()).isEqualTo("New content here");
        assertThat(response.getCategoryId()).isEqualTo(4L);
        verify(categoryClient).getCategoryById(4L);
    }

    @Test
    void unlikePostDoesNotSaveWhenLikesAlreadyZero() {
        // Arrange
        Post post = post(5L, 1L, "PUBLISHED");
        post.setLikesCount(0);
        when(repo.findById(5L)).thenReturn(Optional.of(post));

        // Act
        service.unlikePost(5L);

        // Assert
        verify(repo, never()).save(any());
        assertThat(post.getLikesCount()).isZero();
    }

    @Test
    void likePostIncrementsAndSaves() {
        // Arrange
        Post post = post(6L, 1L, "PUBLISHED");
        post.setLikesCount(2);
        when(repo.findById(6L)).thenReturn(Optional.of(post));

        // Act
        service.likePost(6L);

        // Assert
        assertThat(post.getLikesCount()).isEqualTo(3);
        verify(repo).save(post);
    }

    @Test
    void listQueriesDelegateToRepositoryAndMapResults() {
        // Arrange
        when(repo.findByStatusOrderByPublishedAtDesc("PUBLISHED"))
                .thenReturn(List.of(post(1L, 2L, "PUBLISHED")));
        when(repo.findByAuthorIdOrderByCreatedAtDesc(2L))
                .thenReturn(List.of(post(2L, 2L, "DRAFT")));
        when(repo.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase("spring", "spring"))
                .thenReturn(List.of(post(3L, 4L, "PUBLISHED")));
        when(repo.countByAuthorId(2L)).thenReturn(12);

        // Act + Assert
        assertThat(service.getPublishedPosts()).extracting(PostResponseDTO::getPostId).containsExactly(1L);
        assertThat(service.getPostsByAuthor(2L)).extracting(PostResponseDTO::getPostId).containsExactly(2L);
        assertThat(service.searchPosts("spring")).extracting(PostResponseDTO::getPostId).containsExactly(3L);
        assertThat(service.getPostCountByAuthor(2L)).isEqualTo(12);
    }

    @Test
    void publishUnpublishDeleteAndIncrementViewsDelegateCorrectly() {
        // Arrange
        Post post = post(8L, 1L, "DRAFT");
        when(repo.findById(8L)).thenReturn(Optional.of(post));

        // Act
        service.publishPost(8L);
        service.unpublishPost(8L);
        service.incrementViews(8L);
        service.deletePost(8L);

        // Assert
        assertThat(post.getStatus()).isEqualTo("UNPUBLISHED");
        assertThat(post.getPublishedAt()).isNull();
        assertThat(post.getViewCount()).isEqualTo(1);
        verify(repo).deleteById(8L);
    }

    private static PostRequestDTO requestDto(String title, String content, Long authorId, Long categoryId) {
        PostRequestDTO dto = new PostRequestDTO();
        dto.setTitle(title);
        dto.setContent(content);
        dto.setExcerpt("  excerpt  ");
        dto.setFeaturedImageUrl("  https://cdn.example/post.png  ");
        dto.setAuthorId(authorId);
        dto.setAuthorName("Author Name");
        dto.setAuthorUsername("author");
        dto.setAuthorAvatar("avatar.png");
        dto.setCategoryId(categoryId);
        return dto;
    }

    private static Post post(Long id, Long authorId, String status) {
        Post post = new Post();
        post.setPostId(id);
        post.setAuthorId(authorId);
        post.setAuthorName("Author Name");
        post.setAuthorUsername("author");
        post.setAuthorAvatar("avatar.png");
        post.setCategoryId(100L);
        post.setTitle("Title " + id);
        post.setSlug("title-" + id);
        post.setContent("Content " + id);
        post.setExcerpt("Excerpt " + id);
        post.setFeaturedImageUrl("https://cdn.example/" + id + ".png");
        post.setStatus(status);
        return post;
    }
}
