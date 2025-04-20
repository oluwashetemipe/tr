package com.tredbase.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "payments", uniqueConstraints = {
        @UniqueConstraint(columnNames = "transactionRef")
})
public class Payment {
    @Id
    private UUID id;
    @Column(nullable = false)
    private UUID payingParentId;
    @Column(nullable = false, unique = true)
    private String transactionRef;
    @Column(nullable = false)
    private UUID studentId;
    @Column(nullable = false)
    private BigDecimal originalAmount;
    @Column(nullable = false)
    private BigDecimal totalAmount;
    private BigDecimal fee;
    @Column(nullable = false)
    private UUID initiatorId;
    private LocalDateTime createdAt;

}