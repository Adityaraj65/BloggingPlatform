package com.inkwell.web.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "API-GATEWAY",
        contextId = "notificationClient"
)
public interface NotificationClient {

    @GetMapping("/notifications/recipient/{id}")
    Object getNotifications(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token
    );
}