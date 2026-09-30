package com.hirenest.entity;

import com.hirenest.enums.DocumentType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "employee_documents")
@Getter
@Setter
public class EmployeeDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private Long documentId;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false)
    private DocumentType documentType;

    @Column(name = "document_name", nullable = false)
    private String documentName;

    @Column(name = "upload_date", nullable = false)
    private LocalDate uploadDate;

    // Original uploaded file name
    @Column(name = "file_name")
    private String fileName;

    // File MIME type
    // Example: application/pdf, image/jpeg
    @Column(name = "content_type")
    private String contentType;

    // Actual file stored inside MySQL
    @Lob
    @Column(name = "file_data", columnDefinition = "LONGBLOB")
    private byte[] fileData;

    @Column(name = "status")
    private String status;
}