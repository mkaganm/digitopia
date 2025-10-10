package com.digitopia.userservice.user.api.dto;

import java.util.UUID;

// Minimal user projection for inter-service calls
public record UserDto(UUID id, String email, String status, String fullName) {}
