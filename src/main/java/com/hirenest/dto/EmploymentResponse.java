package com.hirenest.dto;

import com.hirenest.enums.Domain;
import com.hirenest.enums.EmploymentStatus;
import com.hirenest.enums.EmploymentType;

import java.time.LocalDate;

public record EmploymentResponse(
        Long id,
        Long employeeId,
        Domain domain,
        EmploymentType employmentType,
        LocalDate joiningDate,
        EmploymentStatus status,
        Integer probationPeriod,
        LocalDate probationStartDate,
        LocalDate probationEndDate
) {
}
