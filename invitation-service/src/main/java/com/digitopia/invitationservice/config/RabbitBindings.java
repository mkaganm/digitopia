package com.digitopia.invitationservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitBindings {

    @Bean
    public Queue qInvitationNotifications() {
        return new Queue("q.invitation.notifications", true);
    }

    @Bean
    public Binding bInvitationNotifications(Queue qInvitationNotifications, TopicExchange topicExchange) {
        return BindingBuilder.bind(qInvitationNotifications)
                .to(topicExchange)
                .with("invitation.created");
    }
}
