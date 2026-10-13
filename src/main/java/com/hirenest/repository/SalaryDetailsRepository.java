package com.hirenest.repository;

import com.hirenest.dto.CompensationReportResponse;
import com.hirenest.entity.SalaryDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SalaryDetailsRepository
        extends JpaRepository<SalaryDetails, Long> {

    @Query("""
        SELECT new com.hirenest.dto.CompensationReportResponse(
            s.employee.employeeId,
            s.employee.fullName,
            e.employmentType,
            s.salary,
            s.frequency,
            s.currency,
            s.effectiveDate
        )
        FROM SalaryDetails s
        JOIN EmploymentDetails e
            ON e.employee.id = s.employee.id
        ORDER BY s.effectiveDate DESC
    """)
    List<CompensationReportResponse> findCompensationReports();
}