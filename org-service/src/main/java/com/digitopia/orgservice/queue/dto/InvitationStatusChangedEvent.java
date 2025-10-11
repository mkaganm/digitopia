package com.digitopia.orgservice.queue.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record InvitationStatusChangedEvent(
        UUID eventId,
        UUID id,
        UUID organizationId,
        UUID invitedUserId,
        String status,
        OffsetDateTime occurredAt
) {}
