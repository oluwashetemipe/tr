package com.tredbase.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentResponse(
        UUID transactionId,
        UUID studentId,
        UUID payingParentId,
        BigDecimal amount,
        String reference,
        BigDecimal feeRate,
        String message,
        LocalDateTime timestamp
) {}
