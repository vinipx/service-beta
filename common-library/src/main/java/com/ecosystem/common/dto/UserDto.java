package com.ecosystem.common.dto;

import java.time.Instant;

public record UserDto(
        Long id,
        String username,
        String email,
        String firstName,
        String lastName,
        Instant createdAt
) {}
