package com.inkwell.web.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "NOTIFICATION-SERVICE")
public interface NotificationClient {

    @GetMapping("/notifications/recipient/{id}")
    Object getNotifications(@PathVariable Long id,
                            @RequestHeader("Authorization") String token);
}