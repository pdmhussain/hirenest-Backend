package com.hirenest.serviceImpl;

import com.hirenest.dto.EmployeeDocumentRequest;
import com.hirenest.dto.EmployeeDocumentResponse;
import com.hirenest.entity.EmployeeDocument;
import com.hirenest.enums.DocumentType;
import com.hirenest.repository.EmployeeDocumentRepository;
import com.hirenest.service.EmployeeDocumentService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeDocumentServiceImpl
        implements EmployeeDocumentService {

    private final EmployeeDocumentRepository employeeDocumentRepository;


    // =========================================================
    // CREATE / UPLOAD DOCUMENT
    // =========================================================

    @Override
    public EmployeeDocumentResponse createDocument(
            EmployeeDocumentRequest request) {

        try {

            EmployeeDocument document =
                    new EmployeeDocument();

            // Employee ID
            document.setEmployeeId(
                    request.getEmployeeId()
            );

            // Document type
            document.setDocumentType(
                    request.getDocumentType()
            );

            // Document name
            document.setDocumentName(
                    request.getDocumentName()
            );

            // Upload date
            document.setUploadDate(
                    LocalDate.now()
            );

            // Status
            if (request.getStatus() != null
                    && !request.getStatus().isBlank()) {

                document.setStatus(
                        request.getStatus()
                );

            } else {

                document.setStatus("PENDING");
            }


            // =====================================================
            // STORE FILE AS BLOB
            // =====================================================

            MultipartFile file =
                    request.getFile();

            if (file != null && !file.isEmpty()) {

                // Original file name
                document.setFileName(
                        file.getOriginalFilename()
                );

                // MIME type
                document.setContentType(
                        file.getContentType()
                );

                // Actual file content
                document.setFileData(
                        file.getBytes()
                );
            }


            // Save to MySQL
            EmployeeDocument savedDocument =
                    employeeDocumentRepository.save(
                            document
                    );

            return mapToResponse(savedDocument);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to upload document",
                    e
            );
        }
    }


    // =========================================================
    // GET DOCUMENT BY ID
    // =========================================================

    @Override
    public EmployeeDocumentResponse getDocumentById(
            Long documentId) {

        EmployeeDocument document =
                employeeDocumentRepository
                        .findById(documentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Document not found with ID: "
                                                + documentId
                                )
                        );

        return mapToResponse(document);
    }


    // =========================================================
    // GET ALL DOCUMENTS
    // =========================================================

    @Override
    public List<EmployeeDocumentResponse> getAllDocuments() {

        List<EmployeeDocument> documents =
                employeeDocumentRepository.findAll();

        return documents.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    // =========================================================
    // GET DOCUMENTS BY EMPLOYEE ID
    // =========================================================

    @Override
    public List<EmployeeDocumentResponse>
    getDocumentsByEmployeeId(Long employeeId) {

        List<EmployeeDocument> documents =
                employeeDocumentRepository
                        .findByEmployeeId(employeeId);

        return documents.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    // =========================================================
    // GET DOCUMENTS BY TYPE
    // =========================================================

    @Override
    public List<EmployeeDocumentResponse>
    getDocumentsByType(DocumentType documentType) {

        List<EmployeeDocument> documents =
                employeeDocumentRepository
                        .findByDocumentType(documentType);

        return documents.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    // =========================================================
    // UPDATE DOCUMENT
    // =========================================================

    @Override
    public EmployeeDocumentResponse updateDocument(
            Long documentId,
            EmployeeDocumentRequest request) {

        try {

            EmployeeDocument document =
                    employeeDocumentRepository
                            .findById(documentId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Document not found with ID: "
                                                    + documentId
                                    )
                            );


            // =====================================================
            // UPDATE EMPLOYEE ID
            // =====================================================

            if (request.getEmployeeId() != null) {

                document.setEmployeeId(
                        request.getEmployeeId()
                );
            }


            // =====================================================
            // UPDATE DOCUMENT TYPE
            // =====================================================

            if (request.getDocumentType() != null) {

                document.setDocumentType(
                        request.getDocumentType()
                );
            }


            // =====================================================
            // UPDATE DOCUMENT NAME
            // =====================================================

            if (request.getDocumentName() != null
                    && !request.getDocumentName().isBlank()) {

                document.setDocumentName(
                        request.getDocumentName()
                );
            }


            // =====================================================
            // UPDATE STATUS
            // =====================================================

            if (request.getStatus() != null
                    && !request.getStatus().isBlank()) {

                document.setStatus(
                        request.getStatus()
                );
            }


            // =====================================================
            // REPLACE EXISTING BLOB
            // =====================================================

            MultipartFile newFile =
                    request.getFile();

            if (newFile != null
                    && !newFile.isEmpty()) {

                // Replace old file name
                document.setFileName(
                        newFile.getOriginalFilename()
                );

                // Replace MIME type
                document.setContentType(
                        newFile.getContentType()
                );

                // Replace old BLOB with new BLOB
                document.setFileData(
                        newFile.getBytes()
                );

                // Update upload date
                document.setUploadDate(
                        LocalDate.now()
                );
            }


            // Save updated document
            EmployeeDocument updatedDocument =
                    employeeDocumentRepository.save(
                            document
                    );

            return mapToResponse(updatedDocument);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to update document",
                    e
            );
        }
    }


    // =========================================================
    // DELETE DOCUMENT
    // =========================================================

    @Override
    public void deleteDocument(Long documentId) {

        EmployeeDocument document =
                employeeDocumentRepository
                        .findById(documentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Document not found with ID: "
                                                + documentId
                                )
                        );

        // Delete database record.
        // BLOB is deleted automatically with the record.
        employeeDocumentRepository.delete(
                document
        );
    }


    // =========================================================
    // GET ACTUAL FILE / BLOB
    // =========================================================

    @Override
    public byte[] getDocumentFile(
            Long documentId) {

        EmployeeDocument document =
                employeeDocumentRepository
                        .findById(documentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Document not found with ID: "
                                                + documentId
                                )
                        );

        if (document.getFileData() == null
                || document.getFileData().length == 0) {

            throw new RuntimeException(
                    "No file found for document ID: "
                            + documentId
            );
        }

        return document.getFileData();
    }


    // =========================================================
    // GET CONTENT TYPE
    // =========================================================

    @Override
    public String getDocumentContentType(
            Long documentId) {

        EmployeeDocument document =
                employeeDocumentRepository
                        .findById(documentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Document not found with ID: "
                                                + documentId
                                )
                        );

        return document.getContentType();
    }


    // =========================================================
    // GET ORIGINAL FILE NAME
    // =========================================================

    @Override
    public String getDocumentFileName(
            Long documentId) {

        EmployeeDocument document =
                employeeDocumentRepository
                        .findById(documentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Document not found with ID: "
                                                + documentId
                                )
                        );

        return document.getFileName();
    }


    // =========================================================
    // ENTITY → RESPONSE DTO
    // =========================================================

    private EmployeeDocumentResponse mapToResponse(
            EmployeeDocument document) {

        EmployeeDocumentResponse response =
                new EmployeeDocumentResponse();

        response.setDocumentId(
                document.getDocumentId()
        );

        response.setEmployeeId(
                document.getEmployeeId()
        );

        response.setDocumentType(
                document.getDocumentType()
        );

        response.setDocumentName(
                document.getDocumentName()
        );

        response.setUploadDate(
                document.getUploadDate()
        );

        response.setFileName(
                document.getFileName()
        );

        response.setContentType(
                document.getContentType()
        );

        response.setStatus(
                document.getStatus()
        );

        return response;
    }
}