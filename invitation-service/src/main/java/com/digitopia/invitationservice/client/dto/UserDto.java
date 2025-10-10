package com.digitopia.invitationservice.client.dto;

import java.util.UUID;

public record UserDto(UUID id, String email, String status, String fullName) {}
