package com.inkwell.notification.resource;

import com.inkwell.notification.entity.Notification;
import com.inkwell.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationResource {

    @Autowired
    private NotificationService notifService;

    @GetMapping("/recipient/{id}")
    public ResponseEntity<List<Notification>> getByRecipient(@PathVariable Long id) {
        return ResponseEntity.ok(notifService.getByRecipient(id));
    }

    @PutMapping("/mark-all-read")
    public ResponseEntity<Void> markAllRead(@RequestParam Long recipientId) {
        notifService.markAllRead(recipientId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/unread-count/{id}")
    public ResponseEntity<Integer> getUnreadCount(@PathVariable Long id) {
        return ResponseEntity.ok(notifService.getUnreadCount(id));
    }

    @PostMapping("/send-bulk")
    public ResponseEntity<Void> sendBulk(@RequestBody List<Long> ids, @RequestParam String title, @RequestParam String msg) {
        notifService.sendBulk(ids, title, msg);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete-read")
    public ResponseEntity<Void> deleteRead(@RequestParam Long recipientId) {
        notifService.deleteRead(recipientId);
        return ResponseEntity.ok().build();
    }
}