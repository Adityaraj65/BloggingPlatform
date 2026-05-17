package com.inkwell.web.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "API-GATEWAY",
        contextId = "commentClient"
)
public interface CommentClient {

    @PostMapping("/comments")
    Object addComment(
            @RequestBody Object dto,
            @RequestHeader("Authorization") String token
    );
}