package com.inkwell.post.dto;

import lombok.Data;


public class CategoryDTO {
    private Long categoryId;
    private String name;
    private String slug;
    // You only need the fields you plan to use in Post-Service
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSlug() {
		return slug;
	}
	public void setSlug(String slug) {
		this.slug = slug;
	}
}