package com.hirenest.repository;

import com.hirenest.entity.EmployeeDocument;
import com.hirenest.enums.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeDocumentRepository extends JpaRepository<EmployeeDocument, Long> {

    List<EmployeeDocument> findByEmployeeId(String employeeId);

    List<EmployeeDocument> findByDocumentType(DocumentType documentType);
}