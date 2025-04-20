package com.tredbase.backend.model;

import jakarta.persistence.*;
import lombok.Data;


import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name = "students")
public class Student extends Account{
    @Id
    private UUID id;
    private String name;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ParentStudentRelationship> relationships = new HashSet<>();

}