package com.digitopia.invitationservice.service;

import com.digitopia.invitationservice.client.OrgClient;          // validate org existence
import com.digitopia.invitationservice.client.UserClient;         // validate user existence
import com.digitopia.invitationservice.domain.InvitationEntity;
import com.digitopia.invitationservice.repo.InvitationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Orchestrates business rules for invitations:
 * - Validates foreign resources (user, organization)
 * - Enforces "only one PENDING per (user, org)"
 * - Emits AMQP events
 */
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
    private final UserClient userClient;
    private final OrgClient orgClient;

    @Transactional
    public InvitationEntity create(InvitationEntity inv) {
        // Validate foreign resources
        userClient.getById(inv.getInvitedUserId());
        orgClient.getById(inv.getOrganizationId());

        // Only one PENDING per (org, user)
        repo.findByOrganizationIdAndInvitedUserIdAndStatus(
                inv.getOrganizationId(), inv.getInvitedUserId(), STATUS_PENDING
        ).ifPresent(x -> { throw new IllegalArgumentException("Invitation already pending"); });

        // Default status to PENDING if missing
        if (inv.getStatus() == null || inv.getStatus().isBlank()) {
            inv.setStatus(STATUS_PENDING);
        } else if (!ALLOWED_STATUSES.contains(inv.getStatus())) {
            throw new IllegalArgumentException("Invalid status: " + inv.getStatus());
        }

        InvitationEntity saved = repo.save(inv);

        // Emit event for creation
        rabbit.convertAndSend(exchange.getName(), "invitation.created", saved.getId().toString());

        return saved;
    }

    @Transactional
    public InvitationEntity updateStatus(UUID id, String newStatus) {
        if (newStatus == null || !ALLOWED_STATUSES.contains(newStatus)) {
            throw new IllegalArgumentException("Invalid status: " + newStatus);
        }

        InvitationEntity inv = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invitation not found"));

        inv.setStatus(newStatus);
        InvitationEntity updated = repo.save(inv);

        // Optional: emit status change event (keep if you want consumers)
        rabbit.convertAndSend(exchange.getName(), "invitation.status.changed",
                id + ":" + newStatus);

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
