package com.inkwell.post.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.inkwell.post.config.FeignConfig;
import com.inkwell.post.dto.CategoryDTO;

@FeignClient(
        name = "CATEGORY-SERVICE",
        configuration = FeignConfig.class   
)
public interface CategoryClient {

    @GetMapping("/categories/id/{id}")
    CategoryDTO getCategoryById(@PathVariable("id") Long id);
}