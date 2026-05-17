package com.inkwell.notification.dto;

import java.time.LocalDateTime;

public class PostPublishedEvent {

    private Long postId;
    private Long authorId;
    private String authorName;
    private String postTitle;
    private String postSlug;
    private LocalDateTime publishedAt;

    public PostPublishedEvent() {
    }

    public PostPublishedEvent(Long postId, Long authorId, String authorName, String postTitle, String postSlug, LocalDateTime publishedAt) {
        this.postId = postId;
        this.authorId = authorId;
        this.authorName = authorName;
        this.postTitle = postTitle;
        this.postSlug = postSlug;
        this.publishedAt = publishedAt;
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

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostSlug() {
        return postSlug;
    }

    public void setPostSlug(String postSlug) {
        this.postSlug = postSlug;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }
}
