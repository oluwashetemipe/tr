package com.tredbase.backend.web;

import com.tredbase.backend.dto.PaymentRequest;
import com.tredbase.backend.dto.PaymentResponse;
import com.tredbase.backend.exceptions.AccessDeniedException;
import com.tredbase.backend.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/api/payments")
public class PaymentController {


    final private PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/process")
    public ResponseEntity<PaymentResponse> processPayment(@Valid @RequestBody PaymentRequest request) throws AccessDeniedException {
        PaymentResponse response = paymentService.processPayment(
                request
        );
        return ResponseEntity.ok(response);
    }
}
