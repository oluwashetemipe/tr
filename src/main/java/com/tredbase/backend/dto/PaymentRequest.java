package com.tredbase.backend.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentRequest(
        @NotNull(message = "Transaction reference cannot be null")
        @Pattern(regexp = "^[A-Za-z0-9_-]{8,30}$", message = "Transaction reference accepts 8–25 alphanumeric characters, dashes and/or underscores")
        @NotBlank(message = "Transaction reference be blank")
        String transactionRef,
        @NotNull(message = "Student ID cannot be null")
        UUID studentId,
        @NotNull(message = "Paying parent ID cannot be null")
        UUID payingParentId,
        @NotNull(message = "Amount cannot be null")
        @Min(value = 200, message = "Amount cannot be less than 200")
        @Positive(message = "Amount must be a positive number")
        BigDecimal amount,
        @Nullable
        @Positive(message = "fee must be a positive number")
        BigDecimal fee
) {}

