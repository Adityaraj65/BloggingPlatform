package com.inkwell.newsletter.consumer;

import com.inkwell.newsletter.dto.PostPublishedEvent;
import com.inkwell.newsletter.dto.EmailEvent;
import com.inkwell.newsletter.entity.Subscriber;
import com.inkwell.newsletter.repository.SubscriberRepository;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NewsletterConsumer {

    private final SubscriberRepository repository;
    private final RabbitTemplate rabbitTemplate;

    public NewsletterConsumer(SubscriberRepository repository, RabbitTemplate rabbitTemplate) {
        this.repository = repository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "newsletter.queue")
    public void consumePostPublished(PostPublishedEvent event) {
        System.out.println("POST PUBLISHED EVENT RECEIVED in Newsletter for post: " + event.getPostTitle());

        List<Subscriber> activeSubscribers = repository.findByStatus("ACTIVE");

        for (Subscriber s : activeSubscribers) {
            String subject = "New Post: " + event.getPostTitle();
            String body = "Hello " + s.getFullName() + ",\n\n"
                    + event.getAuthorName() + " just published a new post:\n"
                    + event.getPostTitle() + "\n\n"
                    + "Read it here: http://localhost:4200/post/" + event.getPostSlug() + "\n\n"
                    + "To unsubscribe, click: http://localhost:8080/newsletter/unsubscribe?token=" + s.getToken();

            EmailEvent emailEvent = new EmailEvent(s.getEmail(), subject, body);
            
            try {
                rabbitTemplate.convertAndSend("inkwell.events", "email.send", emailEvent);
            } catch (Exception e) {
                // Email sending failure must NOT affect queue consumption or other deliveries
                e.printStackTrace();
            }
        }
    }
}
