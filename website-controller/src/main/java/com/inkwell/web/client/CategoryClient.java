package com.inkwell.web.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "CATEGORY-SERVICE")
public interface CategoryClient {

    @GetMapping("/categories/all")
    Object getAll();
}