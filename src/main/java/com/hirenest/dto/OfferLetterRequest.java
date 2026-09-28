package com.hirenest.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class OfferLetterRequest {

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    @NotBlank(message = "Offer letter number is required")
    private String offerLetterNumber;

    @NotNull(message = "Offer date is required")
    private LocalDate offerDate;

    private String offerLetterFile;

    @NotBlank(message = "Employment type is required")
    private String employmentType;

    @DecimalMin(value = "0.0", message = "Offered salary cannot be negative")
    private BigDecimal offeredSalary;

    @DecimalMin(value = "0.0", message = "Offered stipend cannot be negative")
    private BigDecimal offeredStipend;

    @NotNull(message = "Joining date is required")
    private LocalDate joiningDate;

    @NotBlank(message = "Offer status is required")
    private String offerStatus;
}