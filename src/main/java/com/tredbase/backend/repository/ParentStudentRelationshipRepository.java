package com.tredbase.backend.repository;

import com.tredbase.backend.model.ParentStudentRelationship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ParentStudentRelationshipRepository extends JpaRepository<ParentStudentRelationship, UUID> {

    List<ParentStudentRelationship> findByStudentId(UUID studentId);
    List<ParentStudentRelationship> findByParentId(UUID parentId);
}