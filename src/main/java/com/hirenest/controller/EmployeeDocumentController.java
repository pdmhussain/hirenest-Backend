package com.hirenest.controller;

import com.hirenest.dto.EmployeeDocumentRequest;
import com.hirenest.dto.EmployeeDocumentResponse;
import com.hirenest.enums.DocumentType;
import com.hirenest.service.EmployeeDocumentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee-documents")
@RequiredArgsConstructor
public class EmployeeDocumentController {

    private final EmployeeDocumentService employeeDocumentService;


    // =========================================================
    // CREATE / UPLOAD DOCUMENT
    // =========================================================

    @PostMapping(
            value = "",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<EmployeeDocumentResponse> createDocument(
            @Valid @ModelAttribute EmployeeDocumentRequest request) {

        EmployeeDocumentResponse response =
                employeeDocumentService.createDocument(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // =========================================================
    // GET DOCUMENT BY ID
    // =========================================================

    @GetMapping("/{documentId}")
    public ResponseEntity<EmployeeDocumentResponse> getDocumentById(
            @PathVariable Long documentId) {

        EmployeeDocumentResponse response =
                employeeDocumentService
                        .getDocumentById(documentId);

        return ResponseEntity.ok(response);
    }


    // =========================================================
    // GET ALL DOCUMENTS
    // =========================================================

    @GetMapping
    public ResponseEntity<List<EmployeeDocumentResponse>>
    getAllDocuments() {

        List<EmployeeDocumentResponse> response =
                employeeDocumentService
                        .getAllDocuments();

        return ResponseEntity.ok(response);
    }


    // =========================================================
    // GET DOCUMENTS BY EMPLOYEE ID
    // =========================================================

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<EmployeeDocumentResponse>>
    getDocumentsByEmployeeId(
            @PathVariable Long employeeId) {

        List<EmployeeDocumentResponse> response =
                employeeDocumentService
                        .getDocumentsByEmployeeId(employeeId);

        return ResponseEntity.ok(response);
    }


    // =========================================================
    // GET DOCUMENTS BY DOCUMENT TYPE
    // =========================================================

    @GetMapping("/type/{documentType}")
    public ResponseEntity<List<EmployeeDocumentResponse>>
    getDocumentsByType(
            @PathVariable DocumentType documentType) {

        List<EmployeeDocumentResponse> response =
                employeeDocumentService
                        .getDocumentsByType(documentType);

        return ResponseEntity.ok(response);
    }


    // =========================================================
    // UPDATE DOCUMENT
    // =========================================================

    @PutMapping(
            value = "/{documentId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<EmployeeDocumentResponse> updateDocument(
            @PathVariable Long documentId,
            @Valid @ModelAttribute EmployeeDocumentRequest request) {

        EmployeeDocumentResponse response =
                employeeDocumentService.updateDocument(
                        documentId,
                        request
                );

        return ResponseEntity.ok(response);
    }


    // =========================================================
    // DELETE DOCUMENT
    // =========================================================

    @DeleteMapping("/{documentId}")
    public ResponseEntity<Void> deleteDocument(
            @PathVariable Long documentId) {

        employeeDocumentService.deleteDocument(
                documentId
        );

        return ResponseEntity
                .noContent()
                .build();
    }


    // =========================================================
    // VIEW DOCUMENT
    // =========================================================
    //
    // Browser will try to open the document.
    //
    // Example:
    // GET /api/employee-documents/1/view
    //
    // =========================================================

    @GetMapping("/{documentId}/view")
    public ResponseEntity<byte[]> viewDocument(
            @PathVariable Long documentId) {

        byte[] file =
                employeeDocumentService
                        .getDocumentFile(documentId);

        String contentType =
                employeeDocumentService
                        .getDocumentContentType(documentId);

        MediaType mediaType;

        try {

            mediaType =
                    MediaType.parseMediaType(
                            contentType
                    );

        } catch (Exception e) {

            mediaType =
                    MediaType.APPLICATION_OCTET_STREAM;
        }

        return ResponseEntity
                .ok()
                .contentType(mediaType)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline"
                )
                .body(file);
    }


    // =========================================================
    // DOWNLOAD DOCUMENT
    // =========================================================
    //
    // Browser will download the document.
    //
    // Example:
    // GET /api/employee-documents/1/download
    //
    // =========================================================

    @GetMapping("/{documentId}/download")
    public ResponseEntity<byte[]> downloadDocument(
            @PathVariable Long documentId) {

        byte[] file =
                employeeDocumentService
                        .getDocumentFile(documentId);

        String contentType =
                employeeDocumentService
                        .getDocumentContentType(documentId);

        String fileName =
                employeeDocumentService
                        .getDocumentFileName(documentId);

        MediaType mediaType;

        try {

            mediaType =
                    MediaType.parseMediaType(
                            contentType
                    );

        } catch (Exception e) {

            mediaType =
                    MediaType.APPLICATION_OCTET_STREAM;
        }

        return ResponseEntity
                .ok()
                .contentType(mediaType)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                fileName +
                                "\""
                )
                .body(file);
    }
}