package com.inkwell.notification.service;

import com.inkwell.notification.dto.NotificationEvent;
import com.inkwell.notification.entity.Notification;

import java.util.List;

public interface NotificationService {

    void send(Notification notification);

    void sendBulk(
            List<Long> recipientIds,
            String title,
            String message
    );

    void markAsRead(Long id);

    void markAllRead(Long recipientId);

    void deleteRead(Long recipientId);

    List<Notification> getByRecipient(Long id);

    int getUnreadCount(Long id);

    void deleteNotification(Long id);

    void sendEmail(
            String to,
            String subject,
            String body
    );

    List<Notification> getAll();

    // ================= NEW =================

    void sendEventNotification(
            NotificationEvent event
    );
}