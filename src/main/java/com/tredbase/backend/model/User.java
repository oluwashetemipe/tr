package com.tredbase.backend.model;


import com.tredbase.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;
    @Column
    private String name;
    @Column
    private String password;
    @Column(unique = true)
    private String email;
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

}
