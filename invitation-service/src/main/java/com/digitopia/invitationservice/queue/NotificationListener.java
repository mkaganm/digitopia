package com.digitopia.invitationservice.queue;

import com.digitopia.invitationservice.queue.dto.InvitationCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationListener {

    @RabbitListener(queues = "q.invitation.notifications")
    public void onInvitationCreated(InvitationCreatedEvent evt) {
        log.info("[NOTIFY] Invitation created -> id={}, org={}, user={}, msg={}",
                evt.id(), evt.organizationId(), evt.invitedUserId(), evt.message());
    }
}
