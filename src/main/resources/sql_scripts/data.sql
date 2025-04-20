-- Parents
INSERT INTO parents (id, name, balance) VALUES
                                           ('8f14e45f-ea5e-4a17-b2f3-a8e4aa5b2db1', 'Parent A', 1000.0),
                                           ('f6e7b030-6fa7-4f45-a6e4-62fcbe7bd55e', 'Parent B', 1200.0);

-- Students
INSERT INTO students (id, name, balance) VALUES
                                            ('e2c569be-0f1e-4dd0-a8a6-1e5d309e25b9', 'Student 1 (Shared)', 0.0),
                                            ('db0c7123-1097-42cb-88dc-35439a2f83cd', 'Student 2 (Parent A)', 0.0),
                                            ('9e107d9d-372b-4c2e-a5ee-5e2be5f8c57f', 'Student 3 (Parent B)', 0.0);

-- Relationships
INSERT INTO parent_student_relationship (id, parent_id, student_id,is_active, custody_type, payment_share, created_at) VALUES
                                                                                                     (UUID(), '8f14e45f-ea5e-4a17-b2f3-a8e4aa5b2db1', 'e2c569be-0f1e-4dd0-a8a6-1e5d309e25b9',true, 'shared', 0.5, CURRENT_TIMESTAMP),
                                                                                                     (UUID(), 'f6e7b030-6fa7-4f45-a6e4-62fcbe7bd55e', 'e2c569be-0f1e-4dd0-a8a6-1e5d309e25b9',true, 'shared', 0.5, CURRENT_TIMESTAMP),
                                                                                                     (UUID(), '8f14e45f-ea5e-4a17-b2f3-a8e4aa5b2db1', 'db0c7123-1097-42cb-88dc-35439a2f83cd',true, 'sole', 1.0, CURRENT_TIMESTAMP),
                                                                                                     (UUID(), 'f6e7b030-6fa7-4f45-a6e4-62fcbe7bd55e', '9e107d9d-372b-4c2e-a5ee-5e2be5f8c57f',true,'sole', 1.0, CURRENT_TIMESTAMP);

INSERT INTO users (id, email, password, role)
VALUES ('123e4567-e89b-12d3-a456-426614174000', 'admin@example.com', '$2a$10$W0jwOiVhtjZcQ3L2F6PGheviur8nsv5hsH0Ia.gx6lm3Pfz6ChbE6', 'ADMIN');

