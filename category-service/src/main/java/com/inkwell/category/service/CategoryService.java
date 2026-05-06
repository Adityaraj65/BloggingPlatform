package com.inkwell.category.service;

import com.inkwell.category.dto.CategoryDTO;
import com.inkwell.category.entity.Tag;
import java.util.List;

public interface CategoryService {

    // CATEGORY
    CategoryDTO createCategory(CategoryDTO dto);
    CategoryDTO getBySlug(String slug);
    List<CategoryDTO> getAllCategories();
    CategoryDTO updateCategory(Long id, CategoryDTO dto);
    void deleteCategory(Long id);

    // TAG (using entity directly as per your structure)
    Tag createTag(Tag tag);
    Tag getTagBySlug(String slug);
    List<Tag> getAllTags();
    List<Tag> getTrendingTags();
    void deleteTag(Long id);
}