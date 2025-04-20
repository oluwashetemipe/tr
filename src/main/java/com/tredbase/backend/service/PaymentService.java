package com.tredbase.backend.service;

import com.tredbase.backend.dto.PaymentRequest;
import com.tredbase.backend.dto.PaymentResponse;
import com.tredbase.backend.exceptions.*;
import com.tredbase.backend.model.Parent;
import com.tredbase.backend.model.ParentStudentRelationship;
import com.tredbase.backend.model.Payment;
import com.tredbase.backend.model.Student;
import com.tredbase.backend.repository.ParentRepository;
import com.tredbase.backend.repository.ParentStudentRelationshipRepository;
import com.tredbase.backend.repository.PaymentRepository;
import com.tredbase.backend.repository.StudentRepository;
import com.tredbase.backend.util.JWTUtil;
import com.tredbase.backend.util.TransactionLogger;
//import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class PaymentService {
    private final ParentRepository parentRepository;

    private final StudentRepository studentRepository;

    private final ParentStudentRelationshipRepository relationshipRepository;

    private final PaymentRepository paymentRepository;

    private final JWTUtil jwtUtil;

    private final TransactionLogger transactionLogger;

    public PaymentService(ParentRepository parentRepository, StudentRepository studentRepository, ParentStudentRelationshipRepository relationshipRepository, PaymentRepository paymentRepository, TransactionLogger transactionLogger, TransactionLogger transactionLogger1, JWTUtil jwtUtil) {
        this.parentRepository = parentRepository;
        this.studentRepository = studentRepository;
        this.relationshipRepository = relationshipRepository;
        this.paymentRepository = paymentRepository;
        this.transactionLogger = transactionLogger;
        this.jwtUtil = jwtUtil;
    }


    @Transactional
    public PaymentResponse processPayment(PaymentRequest paymentRequest) throws AccessDeniedException {
        transactionLogger.registerTransactionLogger("processPayment(payment ref=" + paymentRequest.transactionRef() + ", amount=" + paymentRequest.amount() + ")");

        if (paymentRepository.existsByTransactionRef(paymentRequest.transactionRef())) {
            log.warn("Payment ref {} already exists", paymentRequest.transactionRef());
            throw new DuplicateException("Payment already processed for reference: " + paymentRequest.transactionRef());
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userId = "";
        if (authentication != null && authentication.isAuthenticated()) {
            try {
                String token = authentication.getCredentials().toString(); // extract token from credentials
                userId = jwtUtil.extractUserId(token);
            }catch (Exception e) {
               throw new AccessDeniedException("Access denied");
            }
        }


        Student student = studentRepository.findById(paymentRequest.studentId())
                .orElseThrow(() -> new NotFoundException("Student with ID " + paymentRequest.studentId() + " not found"));

        Parent payingParent = parentRepository.findById(paymentRequest.payingParentId())
                .orElseThrow(() -> new NotFoundException("Payment with reference "+ paymentRequest.transactionRef() + " not found"));

        // Get all relationships for this student
        List<ParentStudentRelationship> relationships = relationshipRepository.findByStudentId(paymentRequest.studentId());
        if (relationships.isEmpty() ) {
            throw new NotFoundException("No parent-student relationship found for student: " + paymentRequest.studentId());
        }
        log.info("Applying fee on payment request {}", paymentRequest.transactionRef());
        log.info("Amount before fee: {}", paymentRequest.amount());
        BigDecimal totalAmount;
        // Apply dynamic charge
        BigDecimal feeCharge = (paymentRequest.fee() != null)
                ? paymentRequest.fee()
                : calculateFeeRate(paymentRequest.amount());
        log.info("Payment fee: {}", feeCharge);


        //amount shared between parents after fees
        totalAmount = paymentRequest.amount().multiply(BigDecimal.valueOf(1).add(feeCharge));

        log.info("Amount after fee: {}", totalAmount);
        if (relationships.size() == 1) {
            // Unique child scenario
            ParentStudentRelationship rel = relationships.getFirst();
            if (!rel.getParent().getId().equals(paymentRequest.payingParentId())) {
                log.warn("Parent id mismatch for payment ref {}", paymentRequest.transactionRef());
                throw new BadRequestException("Unauthorized parent for this student");
            }
            debitParentAccount(rel,totalAmount);
        } else {
            // Shared child: update both parents based on their share
            for (ParentStudentRelationship rel : relationships) {
                debitParentAccount(rel, totalAmount);
            }
        }
        //credit student account
        log.info("Student bal before deposit: {}", student.getBalance());
        student.deposit(paymentRequest.amount());
        log.info("Student bal after deposit: {}", student.getBalance());
        studentRepository.save(student);
        //record payment history
        Payment payment = createPaymentRecord(paymentRequest, payingParent, totalAmount, feeCharge, userId);


        return new PaymentResponse(
                payment.getId(),
                student.getId(),
                payment.getPayingParentId(),
                payment.getTotalAmount(),
                payment.getTransactionRef(),
                payment.getFee(),
                "Payment processed successfully",
                LocalDateTime.now()
        );


    }

    private Payment createPaymentRecord(PaymentRequest paymentRequest, Parent payingParent, BigDecimal totalAmount, BigDecimal feeCharge, String userId) {
        Payment payment = new Payment();
        payment.setId(UUID.randomUUID());
        payment.setStudentId(paymentRequest.studentId());
        payment.setPayingParentId(payingParent.getId());
        payment.setOriginalAmount(paymentRequest.amount());
        payment.setTotalAmount(totalAmount);
        payment.setTransactionRef(paymentRequest.transactionRef());
        payment.setFee(feeCharge);
        payment.setInitiatorId(UUID.fromString(userId));
        payment.setCreatedAt(LocalDateTime.now());
        paymentRepository.save(payment);
        return payment;
    }


    private void debitParentAccount(ParentStudentRelationship rel, BigDecimal totalAmount) {
        Parent parent = rel.getParent();
        BigDecimal shareAmount = totalAmount.multiply(rel.getPaymentShare());
        log.info("Payment share amount {} for parent {}", shareAmount ,parent.getName());
        if (shareAmount.compareTo(parent.getBalance()) > 0) {
            throw new InsufficientFundsException("Payment with ID "+ parent.getId()+ " share amount exceeds current account balance");
        }
        log.info("Parent bal before withdrawal: {}", parent.getBalance());
        parent.withdraw(shareAmount);
        log.info("Parent bal after withdrawal: {}", parent.getBalance());
        parentRepository.save(parent);
        log.info("Parent balance updated {}", parent.getBalance());
    }

    private BigDecimal calculateFeeRate(BigDecimal amount) {
        // Default 3% fee
        double rate = 0.03;

        if (amount.compareTo(BigDecimal.valueOf(2000)) >= 0) {
            rate += 0.02; // surcharge for large payments
        }


        return BigDecimal.valueOf(rate);
    }
}

