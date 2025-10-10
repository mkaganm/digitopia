package com.digitopia.invitationservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ configuration for invitations.
 * - Durable TopicExchange ("digitopia.exchange")
 * - Optional queues & bindings for "invitation.created" and "invitation.status.changed"
 */
@Configuration
public class RabbitConfig {

    // --- connection & template ---

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost(System.getenv().getOrDefault("SPRING_RABBITMQ_HOST", "localhost"));
        factory.setPort(Integer.parseInt(System.getenv().getOrDefault("SPRING_RABBITMQ_PORT", "5672")));
        factory.setUsername(System.getenv().getOrDefault("SPRING_RABBITMQ_USERNAME", "guest"));
        factory.setPassword(System.getenv().getOrDefault("SPRING_RABBITMQ_PASSWORD", "guest"));
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
        // If you start sending JSON payloads, set a converter:
        // var tpl = new RabbitTemplate(connectionFactory);
        // tpl.setMessageConverter(new Jackson2JsonMessageConverter());
        // return tpl;
    }

    // --- exchange ---

    @Bean
    public TopicExchange topicExchange() {
        // durable=true, autoDelete=false
        return new TopicExchange("digitopia.exchange", true, false);
    }

    // --- OPTIONAL: queues & bindings (add only if you have/plan consumers) ---

    @Bean
    public Queue invitationCreatedQueue() {
        return new Queue("q.invitation.created", true);
    }

    @Bean
    public Binding bindInvitationCreated(Queue invitationCreatedQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(invitationCreatedQueue)
                .to(topicExchange)
                .with("invitation.created");
    }

    @Bean
    public Queue invitationStatusChangedQueue() {
        return new Queue("q.invitation.status.changed", true);
    }

    @Bean
    public Binding bindInvitationStatusChanged(Queue invitationStatusChangedQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(invitationStatusChangedQueue)
                .to(topicExchange)
                .with("invitation.status.changed");
    }
}
