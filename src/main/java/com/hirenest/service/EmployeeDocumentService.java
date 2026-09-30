package com.hirenest.service;

import com.hirenest.dto.EmployeeDocumentRequest;
import com.hirenest.dto.EmployeeDocumentResponse;
import com.hirenest.enums.DocumentType;

import java.util.List;

public interface EmployeeDocumentService {

    // Create / Upload
    EmployeeDocumentResponse createDocument(
            EmployeeDocumentRequest request
    );

    // Get document information
    EmployeeDocumentResponse getDocumentById(
            Long documentId
    );

    // Get all documents
    List<EmployeeDocumentResponse> getAllDocuments();

    // Get documents by employee
    List<EmployeeDocumentResponse> getDocumentsByEmployeeId(
            Long employeeId
    );

    // Get documents by type
    List<EmployeeDocumentResponse> getDocumentsByType(
            DocumentType documentType
    );

    // Update document
    EmployeeDocumentResponse updateDocument(
            Long documentId,
            EmployeeDocumentRequest request
    );

    // Delete document
    void deleteDocument(
            Long documentId
    );

    // Get actual BLOB
    byte[] getDocumentFile(
            Long documentId
    );

    // Get MIME type
    String getDocumentContentType(
            Long documentId
    );

    // Get original file name
    String getDocumentFileName(
            Long documentId
    );
}