package com.inkwell.notification.consumer;

import com.inkwell.notification.dto.EmailEvent;
import com.inkwell.notification.service.NotificationService;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {

    private final NotificationService notificationService;

    public EmailConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitListener(queues = "email.queue")
    public void consume(EmailEvent event) {
        System.out.println("EMAIL EVENT RECEIVED for: " + event.getTo());
        notificationService.sendEmail(event.getTo(), event.getSubject(), event.getBody());
    }
}
