package com.inkwell.post.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    private Long authorId;

    private Long categoryId;

    @Column(nullable = false)
    private String title;

    // ================= SLUG =================

    @Column(unique = true, nullable = false)
    private String slug;

    // ================= CONTENT =================

    @Column(columnDefinition = "TEXT")
    private String content;

    // ================= EXCERPT =================

    @Column(columnDefinition = "TEXT")
    private String excerpt;

    // ================= IMAGE =================
    // FIXED

    @Column(columnDefinition = "TEXT")
    private String featuredImageUrl;

    // ================= STATUS =================

    @Column(nullable = false)
    private String status;

    // ================= STATS =================

    private int readTimeMin = 1;

    private int viewCount = 0;

    private int likesCount = 0;

    // ================= DATES =================

    private LocalDateTime createdAt =
            LocalDateTime.now();

    private LocalDateTime updatedAt;

    private LocalDateTime publishedAt;
    private String authorName;

    private String authorUsername;

    @Column(columnDefinition = "TEXT")
    private String authorAvatar;

    // ================= GETTERS / SETTERS =================

    public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getAuthorUsername() {
		return authorUsername;
	}

	public void setAuthorUsername(String authorUsername) {
		this.authorUsername = authorUsername;
	}

	public String getAuthorAvatar() {
		return authorAvatar;
	}

	public void setAuthorAvatar(String authorAvatar) {
		this.authorAvatar = authorAvatar;
	}

	public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getFeaturedImageUrl() {
        return featuredImageUrl;
    }

    public void setFeaturedImageUrl(String featuredImageUrl) {
        this.featuredImageUrl = featuredImageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getReadTimeMin() {
        return readTimeMin;
    }

    public void setReadTimeMin(int readTimeMin) {
        this.readTimeMin = readTimeMin;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }
}