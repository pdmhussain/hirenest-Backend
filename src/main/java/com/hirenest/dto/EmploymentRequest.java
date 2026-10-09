package com.hirenest.dto;

import com.hirenest.enums.Domain;
import com.hirenest.enums.EmploymentStatus;
import com.hirenest.enums.EmploymentType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record EmploymentRequest(
        @NotNull(message = "employeeId is required") Long employeeId,
        @NotNull(message = "domain is required") Domain domain,
        @NotNull(message = "employmentType is required") EmploymentType employmentType,
        @NotNull(message = "joiningDate is required") LocalDate joiningDate,
        EmploymentStatus status,
        @Min(value = 0, message = "probationPeriod cannot be negative") Integer probationPeriod,
        LocalDate probationStartDate,
        LocalDate probationEndDate
) {
}
