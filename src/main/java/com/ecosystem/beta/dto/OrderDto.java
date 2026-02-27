package com.ecosystem.beta.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record OrderDto(
        Long id,
        Long userId,
        String productName,
        Integer quantity,
        BigDecimal totalPrice,
        String status,
        Instant createdAt,
        Instant updatedAt
) {}
