package com.tredbase.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;

import java.util.Set;
import java.util.UUID;

@Entity
@Data
@RequiredArgsConstructor
@Table(name = "parents")
public class Parent extends Account{
    @Id
    private UUID id;
    private String name;
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ParentStudentRelationship> relationships = new HashSet<>();
}