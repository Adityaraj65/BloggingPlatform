package com.inkwell.web.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.inkwell.web.dto.PostResponseDTO;

@FeignClient(
        name = "API-GATEWAY",
        contextId = "postClient"
)
public interface PostClient {

    @GetMapping("/posts/published")
    List<PostResponseDTO> getPublished();

    @GetMapping("/posts/{id}")
    PostResponseDTO getById(@PathVariable Long id);

    @PostMapping("/posts")
    PostResponseDTO create(
            @RequestBody Object dto,
            @RequestHeader("Authorization") String token
    );
}