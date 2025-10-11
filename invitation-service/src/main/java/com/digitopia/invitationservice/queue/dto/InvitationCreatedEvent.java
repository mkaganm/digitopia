package com.digitopia.invitationservice.queue.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record InvitationCreatedEvent(
        UUID eventId,
        UUID id,
        UUID organizationId,
        UUID invitedUserId,
        String message,
        String status,
        OffsetDateTime occurredAt
) {}
