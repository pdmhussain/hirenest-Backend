package com.hirenest.service;

import com.hirenest.dto.EmployeeDocumentRequest;
import com.hirenest.dto.EmployeeDocumentResponse;
import com.hirenest.enums.DocumentType;

import java.util.List;

public interface EmployeeDocumentService {


    EmployeeDocumentResponse createDocument(
            EmployeeDocumentRequest request
    );


    EmployeeDocumentResponse getDocumentById(
            Long documentId
    );


    List<EmployeeDocumentResponse> getAllDocuments();


    List<EmployeeDocumentResponse> getDocumentsByEmployeeId(
            Long employeeId
    );


    List<EmployeeDocumentResponse> getDocumentsByType(
            DocumentType documentType
    );


    EmployeeDocumentResponse updateDocument(
            Long documentId,
            EmployeeDocumentRequest request
    );


    void deleteDocument(
            Long documentId
    );


    byte[] getDocumentFile(
            Long documentId
    );


    String getDocumentContentType(
            Long documentId
    );


    String getDocumentFileName(
            Long documentId
    );
}