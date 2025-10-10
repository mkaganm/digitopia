package com.digitopia.invitationservice.service;

import com.digitopia.invitationservice.client.OrgClient;          // optional external validation
import com.digitopia.invitationservice.client.UserClient;         // optional external validation
import com.digitopia.invitationservice.domain.InvitationEntity;
import com.digitopia.invitationservice.exception.ResourceNotFoundException;
import com.digitopia.invitationservice.repo.InvitationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvitationService {

    private static final String STATUS_PENDING  = "PENDING";
    private static final String STATUS_ACCEPTED = "ACCEPTED";
    private static final String STATUS_REJECTED = "REJECTED";
    private static final String STATUS_EXPIRED  = "EXPIRED";

    private static final Set<String> ALLOWED_STATUSES =
            Set.of(STATUS_PENDING, STATUS_ACCEPTED, STATUS_REJECTED, STATUS_EXPIRED);

    private final InvitationRepository repo;
    private final RabbitTemplate rabbit;
    private final TopicExchange exchange;

    // If you don't want external validations, you can remove these fields and related calls.
    private final UserClient userClient;
    private final OrgClient orgClient;

    @Transactional
    public InvitationEntity create(InvitationEntity inv) {
        if (inv.getOrganizationId() == null) {
            throw new IllegalArgumentException("organizationId must be provided");
        }
        if (inv.getInvitedUserId() == null) {
            throw new IllegalArgumentException("invitedUserId must be provided");
        }

        // 1) Duplicate check FIRST (fail fast, no external calls yet)
        repo.findByOrganizationIdAndInvitedUserIdAndStatus(
                inv.getOrganizationId(), inv.getInvitedUserId(), STATUS_PENDING
        ).ifPresent(x -> { throw new IllegalArgumentException("Invitation already pending"); });

        // 2) External validations (optional)
        if (userClient != null) userClient.getById(inv.getInvitedUserId());
        if (orgClient != null)  orgClient.getById(inv.getOrganizationId());

        // 3) Status default/validation
        if (inv.getStatus() == null || inv.getStatus().isBlank()) {
            inv.setStatus(STATUS_PENDING);
        } else if (!ALLOWED_STATUSES.contains(inv.getStatus())) {
            throw new IllegalArgumentException("Invalid status: " + inv.getStatus());
        }

        // 4) Save + publish event
        InvitationEntity saved = repo.save(inv);
        rabbit.convertAndSend(exchange.getName(), "invitation.created", saved.getId().toString());
        return saved;
    }

    @Transactional
    public InvitationEntity updateStatus(UUID id, String newStatus) {
        if (newStatus == null || !ALLOWED_STATUSES.contains(newStatus)) {
            throw new IllegalArgumentException("Invalid status: " + newStatus);
        }

        InvitationEntity inv = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invitation not found: " + id));

        inv.setStatus(newStatus);
        InvitationEntity updated = repo.save(inv);

        rabbit.convertAndSend(exchange.getName(), "invitation.status.changed", id + ":" + newStatus);
        return updated;
    }

    @Transactional(readOnly = true)
    public List<InvitationEntity> getByOrganization(UUID orgId) {
        return repo.findByOrganizationId(orgId);
    }

    @Transactional(readOnly = true)
    public List<InvitationEntity> getByUser(UUID userId) {
        return repo.findByInvitedUserId(userId);
    }
}
