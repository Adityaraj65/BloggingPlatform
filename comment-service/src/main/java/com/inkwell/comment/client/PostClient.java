package com.inkwell.comment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.inkwell.comment.config.FeignConfig;
import com.inkwell.comment.dto.PostDTO;

@FeignClient(
    name = "POST-SERVICE",
    configuration = FeignConfig.class
)
public interface PostClient {

    @GetMapping("/posts/{id}")
    PostDTO getPost(
        @PathVariable("id") Long id
    );
}