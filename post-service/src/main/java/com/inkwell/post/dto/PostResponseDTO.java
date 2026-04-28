package com.inkwell.post.dto;

import java.time.LocalDateTime;

public class PostResponseDTO {
    // 1. Core Identification
    private Long postId;
    private String title;
    private String slug;

    // 2. Content Details
    private String content;
    private String excerpt;
    private String featuredImageUrl;
    private int readTimeMin;

    // 3. Status & Analytics
    private String status;
    private int viewCount;
    private int likesCount;

    // 4. Timestamps (Critical for Figure 3)
    private LocalDateTime createdAt;
    private LocalDateTime publishedAt;
    
    
    private Long categoryId;

    // --- GETTERS AND SETTERS ---

    public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getExcerpt() { return excerpt; }
    public void setExcerpt(String excerpt) { this.excerpt = excerpt; }

    public String getFeaturedImageUrl() { return featuredImageUrl; }
    public void setFeaturedImageUrl(String featuredImageUrl) { this.featuredImageUrl = featuredImageUrl; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getViewCount() { return viewCount; }
    public void setViewCount(int viewCount) { this.viewCount = viewCount; }

    public int getLikesCount() { return likesCount; }
    public void setLikesCount(int likesCount) { this.likesCount = likesCount; }

    public int getReadTimeMin() { return readTimeMin; }
    public void setReadTimeMin(int readTimeMin) { this.readTimeMin = readTimeMin; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getPublishedAt() { return publishedAt; }
    public void setPublishedAt(LocalDateTime publishedAt) { this.publishedAt = publishedAt; }
}