package com.inkwell.comment.config;

import org.springframework.amqp.core.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // ================= EXCHANGE =================

    public static final String EXCHANGE =
            "inkwell.events";

    // ================= QUEUES =================

    public static final String NOTIFICATION_QUEUE =
            "notification.queue";

    public static final String NEWSLETTER_QUEUE =
            "newsletter.queue";

    // ================= ROUTING KEYS =================

    public static final String POST_PUBLISHED =
            "post.published";

    public static final String COMMENT_CREATED =
            "comment.created";

    public static final String COMMENT_REPLY =
            "comment.reply";

    // ================= EXCHANGE =================

    @Bean
    public TopicExchange exchange() {

        return new TopicExchange(EXCHANGE);
    }

    // ================= QUEUES =================

    @Bean
    public Queue notificationQueue() {

        return new Queue(NOTIFICATION_QUEUE);
    }

    @Bean
    public Queue newsletterQueue() {

        return new Queue(NEWSLETTER_QUEUE);
    }

    // ================= BINDINGS =================

    @Bean
    public Binding postPublishedBinding(
            Queue newsletterQueue,
            TopicExchange exchange
    ) {

        return BindingBuilder
                .bind(newsletterQueue)
                .to(exchange)
                .with(POST_PUBLISHED);
    }

    @Bean
    public Binding notificationPostBinding(
            Queue notificationQueue,
            TopicExchange exchange
    ) {

        return BindingBuilder
                .bind(notificationQueue)
                .to(exchange)
                .with(POST_PUBLISHED);
    }

    @Bean
    public Binding commentCreatedBinding(
            Queue notificationQueue,
            TopicExchange exchange
    ) {

        return BindingBuilder
                .bind(notificationQueue)
                .to(exchange)
                .with(COMMENT_CREATED);
    }

    @Bean
    public Binding commentReplyBinding(
            Queue notificationQueue,
            TopicExchange exchange
    ) {

        return BindingBuilder
                .bind(notificationQueue)
                .to(exchange)
                .with(COMMENT_REPLY);
    }
}