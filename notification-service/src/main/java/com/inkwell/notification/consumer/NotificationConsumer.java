package com.inkwell.notification.consumer;

import com.inkwell.notification.dto.NotificationEvent;
import com.inkwell.notification.service.NotificationService;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

    private final NotificationService
            notificationService;

    public NotificationConsumer(
            NotificationService notificationService
    ) {

        this.notificationService =
                notificationService;
    }

    @RabbitListener(queues = "notification.queue")
    public void consume(
            NotificationEvent event
    ) {

        System.out.println(
                "NOTIFICATION RECEIVED: "
                        + event.getMessage()
        );

        notificationService
                .sendEventNotification(event);
    }

}