package com.inkwell.web.client;

import com.inkwell.web.dto.PostResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "POST-SERVICE")
public interface PostClient {

    @GetMapping("/posts/published")
    List<PostResponseDTO> getPublished();

    @GetMapping("/posts/{id}")
    PostResponseDTO getById(@PathVariable Long id);

    @PostMapping("/posts")
    PostResponseDTO create(@RequestBody Object dto,
                           @RequestHeader("Authorization") String token);
}