package com.hirenest.repository;

import com.hirenest.dto.ProbationReportResponse;
import com.hirenest.entity.ProbationDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ProbationDetailsRepository
        extends JpaRepository<ProbationDetails, Long> {

    @Query("""
        SELECT COUNT(p)
        FROM ProbationDetails p
        WHERE LOWER(p.status) = 'in progress'
    """)
    long countProbationInProgress();

    @Query("""
        SELECT COUNT(p)
        FROM ProbationDetails p
        WHERE LOWER(p.status) = 'completed'
    """)
    long countProbationCompleted();

    @Query("""
        SELECT COUNT(p)
        FROM ProbationDetails p
        WHERE p.probationEndDate >= :today
          AND p.probationEndDate <= :dueDate
          AND LOWER(p.status) <> 'completed'
    """)
    long countProbationDueSoon(
            LocalDate today,
            LocalDate dueDate
    );

    @Query("""
        SELECT new com.hirenest.dto.ProbationReportResponse(
            p.employee.employeeId,
            p.employee.fullName,
            p.employee.domain.name,
            e.joiningDate,
            p.probationPeriod,
            p.probationStartDate,
            p.probationEndDate,
            p.status
        )
        FROM ProbationDetails p
        JOIN EmploymentDetails e
            ON e.employee.id = p.employee.id
        WHERE
            (:employee IS NULL OR
             LOWER(p.employee.employeeId) LIKE LOWER(CONCAT('%', :employee, '%')) OR
             LOWER(p.employee.fullName) LIKE LOWER(CONCAT('%', :employee, '%')))
        AND
            (:domain IS NULL OR
             LOWER(p.employee.domain.name) = LOWER(:domain))
        AND
            (:status IS NULL OR
             LOWER(p.status) = LOWER(:status))
        AND
            (:startDate IS NULL OR
             p.probationStartDate >= :startDate)
        AND
            (:endDate IS NULL OR
             p.probationEndDate <= :endDate)
        ORDER BY p.probationStartDate DESC
    """)
    List<ProbationReportResponse> findProbationReports(
            @Param("employee") String employee,
            @Param("domain") String domain,
            @Param("status") String status,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}