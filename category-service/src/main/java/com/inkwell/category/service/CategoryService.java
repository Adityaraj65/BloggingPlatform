package com.inkwell.category.service;

import com.inkwell.category.dto.CategoryDTO;
import com.inkwell.category.entity.Tag;
import java.util.List;

public interface CategoryService {
    // Category Operations
    CategoryDTO createCategory(CategoryDTO dto);
    CategoryDTO getBySlug(String slug);
    List<CategoryDTO> getAllCategories();
    CategoryDTO updateCategory(Long id, CategoryDTO dto);
    void deleteCategory(Long id);

    // Tag Operations
    Tag createTag(Tag tag);
    Tag getTagBySlug(String slug);
    List<Tag> getAllTags();
    void deleteTag(Long id);
    List<Tag> getTrendingTags();

    // Association Logic (Placeholders for now)
    void addTagToPost(Long postId, Long tagId);
    void removeTagFromPost(Long postId, Long tagId);
}