CREATE TABLE parent (
                        id UUID PRIMARY KEY,
                        name VARCHAR(255),
                        balance DECIMAL(19,2)
);

CREATE TABLE student (
                         id UUID PRIMARY KEY,
                         name VARCHAR(255),
                         balance DECIMAL(19,2)
);

CREATE TABLE parent_student_relationship (
                                             parent_id UUID,
                                             student_id UUID,
                                             PRIMARY KEY (parent_id, student_id),
                                             FOREIGN KEY (parent_id) REFERENCES parent(id),
                                             FOREIGN KEY (student_id) REFERENCES student(id)
);

CREATE TABLE payment (
                         id UUID PRIMARY KEY,
                         student_id UUID,
                         paying_parent_id UUID,
                         amount DECIMAL(19,2),
                         dynamic_rate DECIMAL(5,2),
                         adjusted_amount DECIMAL(19,2),
                         created_at TIMESTAMP,
                         fee DECIMAL(19,2),
                         initiator_id UUID

);
CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       email VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(50) NOT NULL
);
