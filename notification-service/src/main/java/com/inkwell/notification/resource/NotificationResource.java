package com.inkwell.notification.resource;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.inkwell.notification.entity.Notification;
import com.inkwell.notification.service.NotificationService;

@RestController
@RequestMapping("/notifications")
@CrossOrigin("*")
public class NotificationResource {

    private final NotificationService notifService;

    public NotificationResource(
            NotificationService notifService
    ) {

        this.notifService = notifService;
    }

    // ================= GET USER NOTIFICATIONS =================

    @GetMapping("/recipient/{id}")
    public ResponseEntity<List<Notification>>
    getByRecipient(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                notifService.getByRecipient(id)
        );
    }

    // ================= UNREAD COUNT =================

    @GetMapping("/unread-count/{id}")
    public ResponseEntity<Integer>
    getUnreadCount(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                notifService.getUnreadCount(id)
        );
    }

    // ================= MARK ALL READ =================

    @PutMapping("/mark-all-read")
    public ResponseEntity<Void>
    markAllRead(
            @RequestParam Long recipientId
    ) {

        notifService.markAllRead(recipientId);

        return ResponseEntity.ok().build();
    }

    // ================= MARK SINGLE READ =================

    @PutMapping("/mark-read/{id}")
    public ResponseEntity<Void>
    markRead(
            @PathVariable Long id
    ) {

        notifService.markAsRead(id);

        return ResponseEntity.ok().build();
    }

    // ================= DELETE NOTIFICATION =================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    deleteNotification(
            @PathVariable Long id
    ) {

        notifService.deleteNotification(id);

        return ResponseEntity.ok().build();
    }

    // ================= DELETE READ =================

    @DeleteMapping("/delete-read")
    public ResponseEntity<Void>
    deleteRead(
            @RequestParam Long recipientId
    ) {

        notifService.deleteRead(recipientId);

        return ResponseEntity.ok().build();
    }

    // ================= SEND EMAIL =================

    @PostMapping("/send-email")
    public ResponseEntity<Void>
    sendEmail(

            @RequestParam String to,

            @RequestParam String subject,

            @RequestParam String body
    ) {

        notifService.sendEmail(
                to,
                subject,
                body
        );

        return ResponseEntity.ok().build();
    }
}