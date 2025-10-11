package com.digitopia.orgservice.queue;

import com.digitopia.orgservice.queue.dto.InvitationStatusChangedEvent;
import com.digitopia.orgservice.repo.MembershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MembershipProjector {

    private final MembershipRepository repo;

    @RabbitListener(queues = "q.membership.projector")
    public void onStatusChanged(InvitationStatusChangedEvent evt) {
        if (!"ACCEPTED".equals(evt.status())) {
            return;
        }
        repo.upsert(UUID.randomUUID(), evt.organizationId(), evt.invitedUserId(), OffsetDateTime.now());
    }
}
