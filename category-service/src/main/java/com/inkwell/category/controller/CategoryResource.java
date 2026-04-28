package com.inkwell.category.controller;

import com.inkwell.category.dto.CategoryDTO;
import com.inkwell.category.entity.Tag;
import com.inkwell.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories") // Gateway is path ko redirect karega
public class CategoryResource {

    @Autowired
    private CategoryService categoryService;

    // --- CATEGORY ENDPOINTS ---

    @PostMapping("/add")
    public ResponseEntity<CategoryDTO> createCat(@RequestBody CategoryDTO dto) {
        return ResponseEntity.ok(categoryService.createCategory(dto));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<CategoryDTO> getBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(categoryService.getBySlug(slug));
    }

    @GetMapping("/all")
    public ResponseEntity<List<CategoryDTO>> getAll() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CategoryDTO> updateCat(@PathVariable Long id, @RequestBody CategoryDTO dto) {
        return ResponseEntity.ok(categoryService.updateCategory(id, dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCat(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    // --- TAG ENDPOINTS ---


    @PostMapping("/tags/add")
    public ResponseEntity<Tag> createTag(@RequestBody Tag tag) {
        return ResponseEntity.ok(categoryService.createTag(tag));
    }

    @GetMapping("/tags/slug/{slug}")
    public ResponseEntity<Tag> getTagBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(categoryService.getTagBySlug(slug));
    }

    @GetMapping("/tags/all")
    public ResponseEntity<List<Tag>> getAllTags() {
        return ResponseEntity.ok(categoryService.getAllTags());
    }

    @GetMapping("/tags/trending")
    public ResponseEntity<List<Tag>> getTrending() {
        return ResponseEntity.ok(categoryService.getTrendingTags());
    }

    @DeleteMapping("/tags/delete/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        categoryService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}