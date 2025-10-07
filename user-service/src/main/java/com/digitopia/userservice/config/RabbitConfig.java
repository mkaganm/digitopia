package com.digitopia.userservice.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

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
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("digitopia.exchange");
    }
}
