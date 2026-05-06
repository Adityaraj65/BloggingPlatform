package com.inkwell.post.dto;

import jakarta.validation.constraints.*;

public class PostRequestDTO {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Content is required")
    private String content;

    private String excerpt;
    private String featuredImageUrl;

    @NotNull(message = "AuthorId is required")
    private Long authorId;

    private Long categoryId;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

    // getters & setters
}