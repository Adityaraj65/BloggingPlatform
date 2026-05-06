package com.inkwell.category.controller;

import com.inkwell.category.dto.CategoryDTO;
import com.inkwell.category.entity.Tag;
import com.inkwell.category.service.CategoryService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryResource {

    private final CategoryService service;

    public CategoryResource(CategoryService service) {
        this.service = service;
    }

    // ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CategoryDTO> create(@Valid @RequestBody CategoryDTO dto) {
        return ResponseEntity.ok(service.createCategory(dto));
    }

    // PUBLIC
    @GetMapping("/slug/{slug}")
    public ResponseEntity<CategoryDTO> get(@PathVariable String slug) {
        return ResponseEntity.ok(service.getBySlug(slug));
    }

    @GetMapping("/all")
    public ResponseEntity<List<CategoryDTO>> getAll() {
        return ResponseEntity.ok(service.getAllCategories());
    }

    // ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> update(@PathVariable Long id,
                                              @Valid @RequestBody CategoryDTO dto) {
        return ResponseEntity.ok(service.updateCategory(id, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteCategory(id);
        return ResponseEntity.ok().build();
    }

    // TAGS
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/tags")
    public ResponseEntity<Tag> createTag(@RequestBody Tag tag) {
        return ResponseEntity.ok(service.createTag(tag));
    }

    @GetMapping("/tags/all")
    public ResponseEntity<List<Tag>> getTags() {
        return ResponseEntity.ok(service.getAllTags());
    }

    @GetMapping("/tags/trending")
    public ResponseEntity<List<Tag>> trending() {
        return ResponseEntity.ok(service.getTrendingTags());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/tags/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        service.deleteTag(id);
        return ResponseEntity.ok().build();
    }
}