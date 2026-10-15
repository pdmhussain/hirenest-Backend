package com.hirenest.dto;

import com.hirenest.enums.DocumentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import com.hirenest.enums.DocumentStatus;



@Getter
@Setter
public class EmployeeDocumentRequest {

    @NotNull(message = "Employee ID is required")
    private String employeeId;

    @NotNull(message = "Document type is required")
    private DocumentType documentType;

    @NotBlank(message = "Document name is required")
    private String documentName;

    private MultipartFile file;

    private DocumentStatus status;
}