package com.inkwell.notification.service;

import com.inkwell.notification.dto.NotificationEvent;
import com.inkwell.notification.entity.Notification;
import com.inkwell.notification.repository.NotificationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImpl
        implements NotificationService {

    @Autowired
    private NotificationRepository repository;

    @Autowired(required = false)
    private JavaMailSender emailSender;

    @Override
    public void send(Notification notification) {

        repository.save(notification);
    }

    @Override
    public void sendBulk(
            List<Long> recipientIds,
            String title,
            String message
    ) {

        List<Notification> list =
                new ArrayList<>();

        for (Long id : recipientIds) {

            Notification n =
                    new Notification();

            n.setRecipientId(id);

            n.setTitle(title);

            n.setMessage(message);

            n.setType("ADMIN_BROADCAST");

            list.add(n);
        }

        repository.saveAll(list);
    }

    @Override
    @Transactional
    public void markAsRead(Long id) {

        repository.findById(id)
                .ifPresent(n -> {

                    n.setRead(true);

                    repository.save(n);
                });
    }

    @Override
    @Transactional
    public void markAllRead(Long recipientId) {

        List<Notification> unread =
                repository
                        .findByRecipientIdAndIsRead(
                                recipientId,
                                false
                        );

        unread.forEach(
                n -> n.setRead(true)
        );

        repository.saveAll(unread);
    }

    @Override
    @Transactional
    public void deleteRead(Long recipientId) {

        repository.deleteByRecipientIdAndIsRead(
                recipientId,
                true
        );
    }

    @Override
    public List<Notification> getByRecipient(
            Long id
    ) {

        return repository.findByRecipientId(id);
    }

    @Override
    public int getUnreadCount(Long id) {

        return repository
                .countByRecipientIdAndIsRead(
                        id,
                        false
                );
    }

    @Override
    public void deleteNotification(Long id) {

        repository.deleteById(id);
    }

    @Override
    public void sendEmail(
            String to,
            String subject,
            String body
    ) {

        try {

            if (emailSender == null) {
                return;
            }

            SimpleMailMessage message =
                    new SimpleMailMessage();

            message.setTo(to);

            message.setSubject(subject);

            message.setText(body);

            emailSender.send(message);

        } catch (Exception e) {

            System.out.println(
                    "Email failed but system continues"
            );
        }
    }

    @Override
    public List<Notification> getAll() {

        return repository.findAll();
    }

    // ================= EVENT NOTIFICATION =================

    @Override
    public void sendEventNotification(
            NotificationEvent event
    ) {

        Notification notification =
                new Notification();

        notification.setRecipientId(
                event.getRecipientId()
        );

        notification.setActorId(
                event.getActorId()
        );

        notification.setType(
                event.getType()
        );

        notification.setTitle(
                event.getTitle()
        );

        notification.setMessage(
                event.getMessage()
        );

        notification.setRelatedId(
                event.getRelatedId()
        );

        notification.setRelatedType(
                event.getRelatedType()
        );

        repository.save(notification);

        System.out.println(
                "NOTIFICATION SAVED"
        );
    }
}