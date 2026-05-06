package com.inkwell.comment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "POST-SERVICE")
public interface PostClient {

    @GetMapping("/posts/{id}")
    Object getPost(@PathVariable("id") Long id);
}