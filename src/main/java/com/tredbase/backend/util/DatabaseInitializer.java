package com.tredbase.backend.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    public DatabaseInitializer(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Insert Parents
        jdbcTemplate.update("INSERT INTO parents (id, name, balance) VALUES (?, ?, ?), (?, ?, ?)",
                "8f14e45f-ea5e-4a17-b2f3-a8e4aa5b2db1", "Parent A", 5000.0,
                "f6e7b030-6fa7-4f45-a6e4-62fcbe7bd55e", "Parent B", 1200.0);

        // Insert Students
        jdbcTemplate.update("INSERT INTO students (id, name, balance) VALUES (?, ?, ?), (?, ?, ?), (?, ?, ?)",
                "e2c569be-0f1e-4dd0-a8a6-1e5d309e25b9", "Student 1 (Shared)", 0.0,
                "db0c7123-1097-42cb-88dc-35439a2f83cd", "Student 2 (Parent A)", 0.0,
                "9e107d9d-372b-4c2e-a5ee-5e2be5f8c57f", "Student 3 (Parent B)", 0.0);

        // Insert Relationships
        jdbcTemplate.update("INSERT INTO parent_student_relationship (id, parent_id, student_id, is_active, custody_type, payment_share, created_at) VALUES (?, ?, ?, ?, ?, ?, ?), (?, ?, ?, ?, ?, ?, ?), (?, ?, ?, ?, ?, ?, ?), (?, ?, ?, ?, ?, ?, ?)",
                UUID.randomUUID(), "8f14e45f-ea5e-4a17-b2f3-a8e4aa5b2db1", "e2c569be-0f1e-4dd0-a8a6-1e5d309e25b9", true, "shared", 0.5, java.time.LocalDateTime.now(),
                UUID.randomUUID(), "f6e7b030-6fa7-4f45-a6e4-62fcbe7bd55e", "e2c569be-0f1e-4dd0-a8a6-1e5d309e25b9", true, "shared", 0.5, java.time.LocalDateTime.now(),
                UUID.randomUUID(), "8f14e45f-ea5e-4a17-b2f3-a8e4aa5b2db1", "db0c7123-1097-42cb-88dc-35439a2f83cd", true, "sole", 1.0, java.time.LocalDateTime.now(),
                UUID.randomUUID(), "f6e7b030-6fa7-4f45-a6e4-62fcbe7bd55e", "9e107d9d-372b-4c2e-a5ee-5e2be5f8c57f", true, "sole", 1.0, java.time.LocalDateTime.now());

        // Insert User (Admin)
        jdbcTemplate.update("INSERT INTO users (id, email, name, password, role) VALUES (?, ?, ?, ?, ?),(?, ?, ?, ?, ?)",
                "123e4567-e89b-12d3-a456-426614174000", "admin@example.com", "Aderonke",
                passwordEncoder.encode("admin123"), "ADMIN", UUID.randomUUID(), "user@example.com", "Seun",
                passwordEncoder.encode("user123"), "CUSTOMER");
    }
}
