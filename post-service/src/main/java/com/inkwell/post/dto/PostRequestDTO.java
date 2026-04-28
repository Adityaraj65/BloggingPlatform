package com.inkwell.post.dto;

public class PostRequestDTO {
    private String title;
    private String content;
    private String excerpt;
    private String featuredImageUrl;
    private Long authorId;
    private int readTimeMin;
    private Long categoryId;

    public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	// Manual Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getExcerpt() { return excerpt; }
    public void setExcerpt(String excerpt) { this.excerpt = excerpt; }
    public String getFeaturedImageUrl() { return featuredImageUrl; }
    public void setFeaturedImageUrl(String featuredImageUrl) { this.featuredImageUrl = featuredImageUrl; }
    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }
    public int getReadTimeMin() { return readTimeMin; }
    public void setReadTimeMin(int readTimeMin) { this.readTimeMin = readTimeMin; }
}