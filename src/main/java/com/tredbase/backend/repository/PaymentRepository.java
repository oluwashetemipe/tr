package com.tredbase.backend.repository;

import com.tredbase.backend.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    boolean existsByTransactionRef(String transactionRef);
}