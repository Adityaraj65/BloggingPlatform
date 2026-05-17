package com.inkwell.category.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.inkwell.category.dto.CategoryDTO;
import com.inkwell.category.dto.TagDTO;
import com.inkwell.category.entity.Category;
import com.inkwell.category.entity.Tag;
import com.inkwell.category.repository.CategoryRepository;
import com.inkwell.category.repository.TagRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepo;
    private final TagRepository tagRepo;

    public CategoryServiceImpl(CategoryRepository categoryRepo, TagRepository tagRepo) {
        this.categoryRepo = categoryRepo;
        this.tagRepo = tagRepo;
    }

    // -------- CATEGORY --------

    @Override
    public CategoryDTO createCategory(CategoryDTO dto) {

        if (categoryRepo.existsByName(dto.getName())) {
            throw new RuntimeException("Category already exists");
        }

        Category c = new Category();
        c.setName(dto.getName());
        c.setDescription(dto.getDescription());
        c.setParentCategoryId(dto.getParentCategoryId());

        // slug generation
        c.setSlug(generateSlug(dto.getName()));

        return mapCategory(categoryRepo.save(c));
    }

    @Override
    public CategoryDTO getBySlug(String slug) {
        Category c = categoryRepo.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return mapCategory(c);
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepo.findAll()
                .stream()
                .map(this::mapCategory)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO updateCategory(Long id, CategoryDTO dto) {

        Category c = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        c.setName(dto.getName());
        c.setDescription(dto.getDescription());

        return mapCategory(categoryRepo.save(c));
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepo.deleteById(id);
    }

    // -------- TAG --------

    @Override
    public TagDTO createTag(TagDTO dto) {

        if (tagRepo.existsByName(dto.getName())) {
            throw new RuntimeException("Tag already exists");
        }

        Tag tag = new Tag();
        tag.setName(dto.getName());
        tag.setSlug(generateSlug(dto.getName()));

        Tag savedTag = tagRepo.save(tag);
        return mapTag(savedTag);
    }

    @Override
    public TagDTO getTagBySlug(String slug) {
        Tag tag = tagRepo.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Tag not found"));
        return mapTag(tag);
    }

    @Override
    public List<TagDTO> getAllTags() {
        return tagRepo.findAll()
                .stream()
                .map(this::mapTag)
                .collect(Collectors.toList());
    }

    @Override
    public List<TagDTO> getTrendingTags() {
        return tagRepo.findTop10ByOrderByPostCountDesc()
                .stream()
                .map(this::mapTag)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteTag(Long id) {
        tagRepo.deleteById(id);
    }
    
    @Override
    public CategoryDTO getById(Long id) {
        Category c = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return mapCategory(c);
    }

    // -------- HELPERS --------

    private String generateSlug(String name) {
        return name.toLowerCase().trim().replaceAll("[^a-z0-9]", "-");
    }

    private CategoryDTO mapCategory(Category c) {
        CategoryDTO dto = new CategoryDTO();
        dto.setCategoryId(c.getCategoryId());
        dto.setName(c.getName());
        dto.setSlug(c.getSlug());
        dto.setDescription(c.getDescription());
        dto.setParentCategoryId(c.getParentCategoryId());
        dto.setPostCount(c.getPostCount());
        dto.setCreatedAt(c.getCreatedAt());
        return dto;
    }

    private TagDTO mapTag(Tag tag) {
        TagDTO dto = new TagDTO();
        dto.setTagId(tag.getTagId());
        dto.setName(tag.getName());
        dto.setSlug(tag.getSlug());
        dto.setPostCount(tag.getPostCount());
        dto.setCreatedAt(tag.getCreatedAt());
        return dto;
    }
}