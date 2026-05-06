package com.inkwell.post.client;

import com.inkwell.post.dto.CategoryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "CATEGORY-SERVICE")
public interface CategoryClient {

    @GetMapping("/categories/id/{id}")
    CategoryDTO getCategoryById(@PathVariable("id") Long id);
}