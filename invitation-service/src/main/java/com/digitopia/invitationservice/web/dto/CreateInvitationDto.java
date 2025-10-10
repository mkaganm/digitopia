package com.digitopia.invitationservice.web.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/**
 * DTO for creating an invitation.
 * Keeps external API decoupled from internal entity structure.
 */
public record CreateInvitationDto(
        @NotNull UUID organizationId,
        @NotNull UUID invitedUserId,
        String message
) {}
