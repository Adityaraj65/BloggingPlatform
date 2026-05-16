package com.inkwell.post.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;

class RabbitMQConfigTest {

    private final RabbitMQConfig config = new RabbitMQConfig();

    @Test
    void createsExchangeAndQueuesWithExpectedNames() {
        // Act
        TopicExchange exchange = config.exchange();
        Queue notificationQueue = config.notificationQueue();
        Queue newsletterQueue = config.newsletterQueue();

        // Assert
        assertThat(exchange.getName()).isEqualTo(RabbitMQConfig.EXCHANGE);
        assertThat(notificationQueue.getName()).isEqualTo(RabbitMQConfig.NOTIFICATION_QUEUE);
        assertThat(newsletterQueue.getName()).isEqualTo(RabbitMQConfig.NEWSLETTER_QUEUE);
    }

//    @Test
//    void createsBindingsWithExpectedRoutingKeys() {
//        // Arrange
//        TopicExchange exchange = config.exchange();
//        Queue notificationQueue = config.notificationQueue();
//        Queue newsletterQueue = config.newsletterQueue();
//
//        // Act
//        Binding postPublished = config.postPublishedBinding(newsletterQueue, exchange);
//        Binding notificationPost = config.notificationPostBinding(notificationQueue, exchange);
//        Binding commentCreated = config.commentCreatedBinding(notificationQueue, exchange);
//        Binding commentReply = config.commentReplyBinding(notificationQueue, exchange);
//
//        // Assert
//        assertThat(postPublished.getRoutingKey()).isEqualTo(RabbitMQConfig.POST_PUBLISHED);
//        assertThat(notificationPost.getRoutingKey()).isEqualTo(RabbitMQConfig.POST_PUBLISHED);
//        assertThat(commentCreated.getRoutingKey()).isEqualTo(RabbitMQConfig.COMMENT_CREATED);
//        assertThat(commentReply.getRoutingKey()).isEqualTo(RabbitMQConfig.COMMENT_REPLY);
//    }
}
