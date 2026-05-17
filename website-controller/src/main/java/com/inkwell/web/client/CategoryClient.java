package com.inkwell.web.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "API-GATEWAY",
        contextId = "categoryClient"
)
public interface CategoryClient {

    @GetMapping("/categories/all")
    Object getAll();
}