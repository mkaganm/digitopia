package com.digitopia.orgservice.client.dto;

import java.util.UUID;

// Minimal fields we need
public record UserDto(UUID id, String email, String status, String fullName) {}
