package com.inkwell.notification.resource;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inkwell.notification.entity.Notification;
import com.inkwell.notification.service.NotificationService;

@RestController
@RequestMapping("/notifications")
public class NotificationResource {

    private final NotificationService notifService;

    public NotificationResource(NotificationService notifService) {
        this.notifService = notifService;
    }

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

    // ADMIN ONLY
//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/send-bulk")
    public ResponseEntity<Void> sendBulk(@RequestBody List<Long> ids,
                                         @RequestParam String title,
                                         @RequestParam String msg) {
        notifService.sendBulk(ids, title, msg);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete-read")
    public ResponseEntity<Void> deleteRead(@RequestParam Long recipientId) {
        notifService.deleteRead(recipientId);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/send-email")
    public ResponseEntity<Void> sendEmail(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String body) {

        notifService.sendEmail(to, subject, body);

        return ResponseEntity.ok().build();
    }
}