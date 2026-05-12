package com.inkwell.comment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.inkwell.comment.config.FeignConfig;

@FeignClient(
    name = "POST-SERVICE",
    configuration = FeignConfig.class
)
public interface PostClient {

    @GetMapping("/posts/{id}")
    Object getPost(
        @PathVariable("id") Long id
    );
}