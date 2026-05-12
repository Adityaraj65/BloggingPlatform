package com.inkwell.category.service;

import java.util.List;

import com.inkwell.category.dto.CategoryDTO;
import com.inkwell.category.dto.TagDTO;

public interface CategoryService {

    // CATEGORY
    CategoryDTO createCategory(CategoryDTO dto);
    CategoryDTO getBySlug(String slug);
    List<CategoryDTO> getAllCategories();
    CategoryDTO updateCategory(Long id, CategoryDTO dto);
    void deleteCategory(Long id);

    // TAG
    TagDTO createTag(TagDTO tag);
    TagDTO getTagBySlug(String slug);
    List<TagDTO> getAllTags();
    List<TagDTO> getTrendingTags();
    void deleteTag(Long id);
    CategoryDTO getById(Long id);
}