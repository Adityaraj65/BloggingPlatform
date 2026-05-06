package com.inkwell.newsletter.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "NOTIFICATION-SERVICE")
public interface NotificationClient {

    @PostMapping("/notifications/send-email")
    void sendEmail(@RequestParam String to,
                   @RequestParam String subject,
                   @RequestParam String body);
}