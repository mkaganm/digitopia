package com.digitopia.invitationservice.service;

import com.digitopia.invitationservice.domain.InvitationEntity;
import com.digitopia.invitationservice.repo.InvitationRepository;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class InvitationService {
    private final InvitationRepository repo;
    private final RabbitTemplate rabbit;
    private final TopicExchange exchange;

    public InvitationService(InvitationRepository repo, RabbitTemplate rabbit, TopicExchange exchange) {
        this.repo = repo;
        this.rabbit = rabbit;
        this.exchange = exchange;
    }

    @Transactional
    public InvitationEntity create(InvitationEntity inv) {
        repo.findByOrganizationIdAndInvitedUserIdAndStatus(inv.getOrganizationId(), inv.getInvitedUserId(), "PENDING")
                .ifPresent(x -> { throw new IllegalArgumentException("Invitation already pending"); });

        InvitationEntity saved = repo.save(inv);
        rabbit.convertAndSend(exchange.getName(), "invitation.created", saved.getId().toString());
        return saved;
    }

    @Transactional
    public InvitationEntity updateStatus(UUID id, String newStatus) {
        InvitationEntity inv = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invitation not found"));
        inv.setStatus(newStatus);
        return repo.save(inv);
    }

    public List<InvitationEntity> getByOrganization(UUID orgId) { return repo.findByOrganizationId(orgId); }
    public List<InvitationEntity> getByUser(UUID userId) { return repo.findByInvitedUserId(userId); }
}
