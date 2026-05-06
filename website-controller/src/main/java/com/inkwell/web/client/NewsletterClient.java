package com.inkwell.web.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "NEWSLETTER-SERVICE")
public interface NewsletterClient {

    @PostMapping("/newsletter/subscribe")
    Object subscribe(@RequestBody Object request);
}