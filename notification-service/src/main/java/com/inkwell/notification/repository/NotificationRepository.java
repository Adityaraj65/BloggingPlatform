package com.inkwell.notification.repository;

import com.inkwell.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientId(Long recipientId);
    List<Notification> findByRecipientIdAndIsRead(Long recipientId, boolean isRead);
    int countByRecipientIdAndIsRead(Long recipientId, boolean isRead);
    List<Notification> findByType(String type);
    List<Notification> findByRelatedId(Long relatedId);
    void deleteByNotificationId(Long notificationId);
    void deleteByRecipientIdAndIsRead(Long recipientId, boolean isRead);
}