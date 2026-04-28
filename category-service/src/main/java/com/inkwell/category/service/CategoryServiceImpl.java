package com.inkwell.category.service;

import com.inkwell.category.dto.CategoryDTO;
import com.inkwell.category.entity.Category;
import com.inkwell.category.entity.Tag;
import com.inkwell.category.repository.CategoryRepository;
import com.inkwell.category.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TagRepository tagRepository;

    // --- CATEGORY LOGIC ---

    @Override
    public CategoryDTO createCategory(CategoryDTO dto) {
        Category category = new Category();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setParentCategoryId(dto.getParentCategoryId());
        
        // Auto Slug Generation
        category.setSlug(dto.getName().toLowerCase().trim().replaceAll("[^a-z0-9]", "-"));
        
        Category saved = categoryRepository.save(category);
        return mapToCategoryDTO(saved);
    }

    @Override
    public CategoryDTO getBySlug(String slug) {
        Category cat = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return mapToCategoryDTO(cat);
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::mapToCategoryDTO).collect(Collectors.toList());
    }

    @Override
    public CategoryDTO updateCategory(Long id, CategoryDTO dto) {
        Category cat = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        cat.setName(dto.getName());
        cat.setDescription(dto.getDescription());
        return mapToCategoryDTO(categoryRepository.save(cat));
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    // --- TAG LOGIC ---

    @Override
    public Tag createTag(Tag tag) {
        tag.setSlug(tag.getName().toLowerCase().trim().replaceAll("[^a-z0-9]", "-"));
        return tagRepository.save(tag);
    }

    @Override
    public Tag getTagBySlug(String slug) {
        return tagRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Tag not found"));
    }

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public List<Tag> getTrendingTags() {
        return tagRepository.findTop10ByOrderByPostCountDesc();
    }

    @Override
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }

    // Association Placeholders (Will be expanded when linking with Post-Service)
    @Override public void addTagToPost(Long pId, Long tId) {}
    @Override public void removeTagFromPost(Long pId, Long tId) {}

    // Helper Mapper
    private CategoryDTO mapToCategoryDTO(Category cat) {
        CategoryDTO dto = new CategoryDTO();
        dto.setCategoryId(cat.getCategoryId());
        dto.setName(cat.getName());
        dto.setSlug(cat.getSlug());
        dto.setDescription(cat.getDescription());
        dto.setParentCategoryId(cat.getParentCategoryId());
        dto.setPostCount(cat.getPostCount());
        dto.setCreatedAt(cat.getCreatedAt());
        return dto;
    }
}