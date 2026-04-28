package com.inkwell.post.client;

import com.inkwell.post.dto.CategoryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "CATEGORY-SERVICE")
public interface CategoryClient {
    
    // Internal call to validate if category exists
    @GetMapping("/categories/slug/{slug}")
    CategoryDTO getCategoryBySlug(@PathVariable("slug") String slug);

    // If Category-Service has a direct ID lookup endpoint
    @GetMapping("/categories/id/{id}")
    CategoryDTO getCategoryById(@PathVariable("id") Long id);
}