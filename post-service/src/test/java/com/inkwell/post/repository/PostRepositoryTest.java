package com.inkwell.post.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import com.inkwell.post.entity.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest(properties = {
        "eureka.client.enabled=false",
        "spring.cloud.discovery.enabled=false"
})
class PostRepositoryTest {

    @Autowired
    private PostRepository repository;

    @Test
    void findBySlugReturnsMatchingPost() {
        // Arrange
        Post post = repository.save(post(1L, "Spring Tips", "spring-tips", "PUBLISHED"));

        // Act + Assert
        assertThat(repository.findBySlug("spring-tips"))
                .isPresent()
                .get()
                .extracting(Post::getPostId)
                .isEqualTo(post.getPostId());
    }

    @Test
    void findByAuthorAndStatusUseExpectedOrdering() {
        // Arrange
        Post older = post(3L, "Older", "older", "PUBLISHED");
        older.setCreatedAt(LocalDateTime.now().minusDays(2));
        older.setPublishedAt(LocalDateTime.now().minusDays(2));
        Post newer = post(3L, "Newer", "newer", "PUBLISHED");
        newer.setCreatedAt(LocalDateTime.now());
        newer.setPublishedAt(LocalDateTime.now());
        repository.save(older);
        repository.save(newer);

        // Act + Assert
        assertThat(repository.findByAuthorIdOrderByCreatedAtDesc(3L))
                .extracting(Post::getTitle)
                .containsExactly("Newer", "Older");
        assertThat(repository.findByStatusOrderByPublishedAtDesc("PUBLISHED"))
                .extracting(Post::getTitle)
                .containsExactly("Newer", "Older");
    }

    @Test
    void searchAndCountByAuthorReturnMatchingRows() {
        // Arrange
        repository.save(post(5L, "Java Basics", "java-basics", "DRAFT"));
        Post springPost = post(5L, "Other", "other", "DRAFT");
        springPost.setContent("Spring Boot testing");
        repository.save(springPost);
        repository.save(post(6L, "Unrelated", "unrelated", "DRAFT"));

        // Act + Assert
        assertThat(repository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase("spring", "spring"))
                .extracting(Post::getSlug)
                .containsExactly("other");
        assertThat(repository.countByAuthorId(5L)).isEqualTo(2);
    }

    private static Post post(Long authorId, String title, String slug, String status) {
        Post post = new Post();
        post.setAuthorId(authorId);
        post.setCategoryId(10L);
        post.setTitle(title);
        post.setSlug(slug);
        post.setContent("Content for " + title);
        post.setExcerpt("Excerpt");
        post.setFeaturedImageUrl("https://cdn.example/" + slug + ".png");
        post.setStatus(status);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        post.setPublishedAt("PUBLISHED".equals(status) ? LocalDateTime.now() : null);
        return post;
    }
}
