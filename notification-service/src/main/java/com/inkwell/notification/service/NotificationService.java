package com.inkwell.notification.service;

import com.inkwell.notification.entity.Notification;
import java.util.List;
import java.util.Optional;

public interface NotificationService {
    void send(Notification notification);
    void sendBulk(List<Long> recipientIds, String title, String message);
    void markAsRead(Long notificationId);
    void markAllRead(Long recipientId);
    void deleteRead(Long recipientId);
    List<Notification> getByRecipient(Long recipientId);
    int getUnreadCount(Long recipientId);
    void deleteNotification(Long notificationId);
    void sendEmail(String to, String subject, String body);
    List<Notification> getAll();
}