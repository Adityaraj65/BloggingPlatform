package com.inkwell.web.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "COMMENT-SERVICE")
public interface CommentClient {

    @PostMapping("/comments/add")
    Object addComment(@RequestBody Object dto,
                      @RequestHeader("Authorization") String token);
}