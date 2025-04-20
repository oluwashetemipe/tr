package com.tredbase.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.UUID;

@Entity
@ToString
@Data
@RequiredArgsConstructor
@Table(name = "parent_student_relationship")
public class ParentStudentRelationship {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    private Parent parent;

    @ManyToOne(optional = false)
    private Student student;

    private boolean isActive = true;

    private String custodyType; // e.g., "shared", "sole/unique"

    private BigDecimal paymentShare;

    private LocalDateTime createdAt = LocalDateTime.now();

}
