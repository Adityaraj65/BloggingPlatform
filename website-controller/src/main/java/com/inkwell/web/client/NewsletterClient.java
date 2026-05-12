package com.inkwell.web.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "API-GATEWAY",
        contextId = "newsletterClient"
)
public interface NewsletterClient {

    @PostMapping("/newsletter/subscribe")
    Object subscribe(@RequestBody Object request);
}