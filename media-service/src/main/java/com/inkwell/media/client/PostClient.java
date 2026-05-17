package com.inkwell.media.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "POST-SERVICE")
public interface PostClient {

    @GetMapping("/posts/{id}")
    Object getPostById(@PathVariable("id") Long id);
}