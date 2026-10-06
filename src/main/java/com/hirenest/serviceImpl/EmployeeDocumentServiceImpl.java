package com.hirenest.serviceImpl;

import com.hirenest.dto.EmployeeDocumentRequest;
import com.hirenest.dto.EmployeeDocumentResponse;
import com.hirenest.entity.EmployeeDocument;
import com.hirenest.enums.DocumentType;
import com.hirenest.exception.BadRequestException;
import com.hirenest.exception.ResourceNotFoundException;
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

    @Override
    public EmployeeDocumentResponse createDocument(
            EmployeeDocumentRequest request) {

        try {

            // Validate file
            MultipartFile file = request.getFile();

            if (file == null || file.isEmpty()) {
                throw new BadRequestException(
                        "Document file is required");
            }

            EmployeeDocument document =
                    new EmployeeDocument();

            document.setEmployeeId(
                    request.getEmployeeId()
            );

            document.setDocumentType(
                    request.getDocumentType()
            );

            document.setDocumentName(
                    request.getDocumentName()
            );

            document.setUploadDate(
                    LocalDate.now()
            );

            // Set status
            if (request.getStatus() != null
                    && !request.getStatus().isBlank()) {

                document.setStatus(
                        request.getStatus()
                );

            } else {

                document.setStatus("PENDING");
            }

            // File information
            document.setFileName(
                    file.getOriginalFilename()
            );

            document.setContentType(
                    file.getContentType()
            );

            document.setFileData(
                    file.getBytes()
            );

            EmployeeDocument savedDocument =
                    employeeDocumentRepository.save(
                            document
                    );

            return mapToResponse(savedDocument);

        } catch (IOException e) {

            throw new BadRequestException(
                    "Failed to upload document"
            );
        }
    }

    @Override
    public EmployeeDocumentResponse getDocumentById(
            Long documentId) {

        EmployeeDocument document =
                employeeDocumentRepository
                        .findById(documentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Document not found with ID: "
                                                + documentId
                                )
                        );

        return mapToResponse(document);
    }

    @Override
    public List<EmployeeDocumentResponse> getAllDocuments() {

        List<EmployeeDocument> documents =
                employeeDocumentRepository.findAll();

        return documents.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

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

    @Override
    public List<EmployeeDocumentResponse>
    getDocumentsByType(DocumentType documentType) {

        if (documentType == null) {
            throw new BadRequestException(
                    "Document type is required"
            );
        }

        List<EmployeeDocument> documents =
                employeeDocumentRepository
                        .findByDocumentType(documentType);

        return documents.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDocumentResponse updateDocument(
            Long documentId,
            EmployeeDocumentRequest request) {

        try {

            EmployeeDocument document =
                    employeeDocumentRepository
                            .findById(documentId)
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Document not found with ID: "
                                                    + documentId
                                    )
                            );

            // Update employee ID if provided
            if (request.getEmployeeId() != null) {

                document.setEmployeeId(
                        request.getEmployeeId()
                );
            }

            // Update document type
            if (request.getDocumentType() != null) {

                document.setDocumentType(
                        request.getDocumentType()
                );
            }

            // Update document name
            if (request.getDocumentName() != null
                    && !request.getDocumentName().isBlank()) {

                document.setDocumentName(
                        request.getDocumentName()
                );
            }

            // Update status
            if (request.getStatus() != null
                    && !request.getStatus().isBlank()) {

                document.setStatus(
                        request.getStatus()
                );
            }

            // Replace file only if new file is provided
            MultipartFile newFile =
                    request.getFile();

            if (newFile != null
                    && !newFile.isEmpty()) {

                document.setFileName(
                        newFile.getOriginalFilename()
                );

                document.setContentType(
                        newFile.getContentType()
                );

                document.setFileData(
                        newFile.getBytes()
                );

                document.setUploadDate(
                        LocalDate.now()
                );
            }

            EmployeeDocument updatedDocument =
                    employeeDocumentRepository.save(
                            document
                    );

            return mapToResponse(updatedDocument);

        } catch (IOException e) {

            throw new BadRequestException(
                    "Failed to update document"
            );
        }
    }

    @Override
    public void deleteDocument(Long documentId) {

        EmployeeDocument document =
                employeeDocumentRepository
                        .findById(documentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Document not found with ID: "
                                                + documentId
                                )
                        );

        employeeDocumentRepository.delete(
                document
        );
    }

    @Override
    public byte[] getDocumentFile(
            Long documentId) {

        EmployeeDocument document =
                employeeDocumentRepository
                        .findById(documentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Document not found with ID: "
                                                + documentId
                                )
                        );

        if (document.getFileData() == null
                || document.getFileData().length == 0) {

            throw new BadRequestException(
                    "No file found for document ID: "
                            + documentId
            );
        }

        return document.getFileData();
    }

    @Override
    public String getDocumentContentType(
            Long documentId) {

        EmployeeDocument document =
                employeeDocumentRepository
                        .findById(documentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Document not found with ID: "
                                                + documentId
                                )
                        );

        return document.getContentType();
    }


    @Override
    public String getDocumentFileName(
            Long documentId) {

        EmployeeDocument document =
                employeeDocumentRepository
                        .findById(documentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Document not found with ID: "
                                                + documentId
                                )
                        );

        return document.getFileName();
    }

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