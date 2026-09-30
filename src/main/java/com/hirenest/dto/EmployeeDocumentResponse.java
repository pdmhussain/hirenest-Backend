package com.hirenest.dto;

import com.hirenest.enums.DocumentType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EmployeeDocumentResponse {

    private Long documentId;

    private Long employeeId;

    private DocumentType documentType;

    private String documentName;

    private LocalDate uploadDate;

    private String fileName;

    private String contentType;

    private String status;
}