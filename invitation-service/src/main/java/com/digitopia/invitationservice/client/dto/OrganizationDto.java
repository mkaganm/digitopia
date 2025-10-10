package com.digitopia.invitationservice.client.dto;

import java.util.UUID;

public record OrganizationDto(UUID id, String name, UUID ownerId) {}
