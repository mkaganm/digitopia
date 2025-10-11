package com.digitopia.orgservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitBindings {

    @Bean
    public Queue qMembershipProjector() {
        return new Queue("q.membership.projector", true);
    }

    @Bean
    public Binding bMembershipProjector(Queue qMembershipProjector, TopicExchange topicExchange) {
        return BindingBuilder.bind(qMembershipProjector)
                .to(topicExchange)
                .with("invitation.status.changed");
    }
}
