package com.inkwell.newsletter.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
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

    public static final String EMAIL_SEND =
            "email.send";

    // ================= JSON CONVERTER =================

    @Bean
    public MessageConverter jsonMessageConverter(com.fasterxml.jackson.databind.ObjectMapper objectMapper) {

        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(objectMapper);
        org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper typeMapper = new org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper();
        typeMapper.setTrustedPackages("*");
        typeMapper.setTypePrecedence(org.springframework.amqp.support.converter.Jackson2JavaTypeMapper.TypePrecedence.INFERRED);
        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }

    // ================= EXCHANGE =================

    @Bean
    TopicExchange exchange() {

        return new TopicExchange(EXCHANGE);
    }

    // ================= QUEUES =================

    @Bean
    Queue notificationQueue() {

        return new Queue(NOTIFICATION_QUEUE);
    }

    @Bean
    Queue newsletterQueue() {

        return new Queue(NEWSLETTER_QUEUE);
    }

    // ================= BINDINGS =================

    @Bean
    Binding postPublishedBinding(
            Queue newsletterQueue,
            TopicExchange exchange
    ) {

        return BindingBuilder
                .bind(newsletterQueue)
                .to(exchange)
                .with(POST_PUBLISHED);
    }

    @Bean
    Binding notificationPostBinding(
            Queue notificationQueue,
            TopicExchange exchange
    ) {

        return BindingBuilder
                .bind(notificationQueue)
                .to(exchange)
                .with(POST_PUBLISHED);
    }

    @Bean
    Binding commentCreatedBinding(
            Queue notificationQueue,
            TopicExchange exchange
    ) {

        return BindingBuilder
                .bind(notificationQueue)
                .to(exchange)
                .with(COMMENT_CREATED);
    }

    @Bean
    Binding commentReplyBinding(
            Queue notificationQueue,
            TopicExchange exchange
    ) {

        return BindingBuilder
                .bind(notificationQueue)
                .to(exchange)
                .with(COMMENT_REPLY);
    }
}