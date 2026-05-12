package com.inkwell.category.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inkwell.category.dto.CategoryDTO;
import com.inkwell.category.dto.TagDTO;
import com.inkwell.category.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/categories")
public class CategoryResource {

    private final CategoryService service;

    public CategoryResource(CategoryService service) {
        this.service = service;
    }

    // ================= CREATE =================
//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CategoryDTO> create(@Valid @RequestBody CategoryDTO dto) {
        return ResponseEntity.ok(service.createCategory(dto));
    }

    // ================= GET BY ID (🔥 MISSING FIX) =================
    // 👉 THIS IS THE MAIN FIX
    @GetMapping("/id/{id}")
    public ResponseEntity<CategoryDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // ================= GET BY SLUG =================
    @GetMapping("/slug/{slug}")
    public ResponseEntity<CategoryDTO> get(@PathVariable String slug) {
        return ResponseEntity.ok(service.getBySlug(slug));
    }

    // ================= GET ALL =================
    @GetMapping("/all")
    public ResponseEntity<List<CategoryDTO>> getAll() {
        return ResponseEntity.ok(service.getAllCategories());
    }

    // ================= UPDATE =================
//    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> update(@PathVariable Long id,
                                              @Valid @RequestBody CategoryDTO dto) {
        return ResponseEntity.ok(service.updateCategory(id, dto));
    }

    // ================= DELETE =================
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteCategory(id);
        return ResponseEntity.ok().build();
    }

    // ================= TAGS =================
//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/tags")
    public ResponseEntity<TagDTO> createTag(@Valid @RequestBody TagDTO tag) {
        return ResponseEntity.ok(service.createTag(tag));
    }

    @GetMapping("/tags/all")
    public ResponseEntity<List<TagDTO>> getTags() {
        return ResponseEntity.ok(service.getAllTags());
    }

    @GetMapping("/tags/trending")
    public ResponseEntity<List<TagDTO>> trending() {
        return ResponseEntity.ok(service.getTrendingTags());
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/tags/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        service.deleteTag(id);
        return ResponseEntity.ok().build();
    }
}