package com.hirenest.repository;

import com.hirenest.dto.EmployeeReportResponse;
import com.hirenest.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("""
        SELECT new com.hirenest.dto.EmployeeReportResponse(
            e.employeeId,
            e.fullName,
            e.email,
            e.phone,
            e.domain.name,
            ed.employmentType,
            ed.joiningDate,
            e.status
        )
        FROM Employee e
        LEFT JOIN EmploymentDetails ed
            ON ed.employee.id = e.id
        WHERE
            (:employeeId IS NULL OR
             LOWER(e.employeeId) LIKE LOWER(CONCAT('%', :employeeId, '%')))
        AND
            (:name IS NULL OR
             LOWER(e.fullName) LIKE LOWER(CONCAT('%', :name, '%')))
        AND
            (:email IS NULL OR
             LOWER(e.email) LIKE LOWER(CONCAT('%', :email, '%')))
        AND
            (:phone IS NULL OR
             e.phone LIKE CONCAT('%', :phone, '%'))
        AND
            (:domain IS NULL OR
             LOWER(e.domain.name) = LOWER(:domain))
        AND
            (:employmentType IS NULL OR
             LOWER(ed.employmentType) = LOWER(:employmentType))
        AND
            (:joiningDate IS NULL OR
             ed.joiningDate = :joiningDate)
        AND
            (:status IS NULL OR
             LOWER(e.status) = LOWER(:status))
        ORDER BY ed.joiningDate DESC
    """)
    List<EmployeeReportResponse> searchEmployees(
            @Param("employeeId") String employeeId,
            @Param("name") String name,
            @Param("email") String email,
            @Param("phone") String phone,
            @Param("domain") String domain,
            @Param("employmentType") String employmentType,
            @Param("joiningDate") LocalDate joiningDate,
            @Param("status") String status
    );
}