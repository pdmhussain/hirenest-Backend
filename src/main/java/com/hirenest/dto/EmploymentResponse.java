package com.hirenest.dto;

import java.time.LocalDate;

import com.hirenest.enums.Domain;
import com.hirenest.enums.EmploymentStatus;
import com.hirenest.enums.EmploymentType;

public record EmploymentResponse(
        Long id,
        String employeeId,
        Domain domain,
        EmploymentType employmentType,
        LocalDate joiningDate,
        EmploymentStatus status,
        Integer probationPeriod,
        LocalDate probationStartDate,
        LocalDate probationEndDate
) {
}
