package com.hirenest.repository;

import com.hirenest.dto.RecentEmployeeResponse;
import com.hirenest.dto.RecentRecordResponse;
import com.hirenest.entity.EmploymentDetails;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmploymentDetailsRepository
        extends JpaRepository<EmploymentDetails, Long> {

    @Query("""
        SELECT COUNT(e)
        FROM EmploymentDetails e
        WHERE LOWER(e.employmentType) LIKE '%intern%'
    """)
    long countInterns();

    @Query("""
        SELECT COUNT(e)
        FROM EmploymentDetails e
        WHERE LOWER(e.employmentType) LIKE '%full%'
    """)
    long countFullTimeEmployees();

    @Query("""
        SELECT new com.hirenest.dto.RecentEmployeeResponse(
            e.employee.id,
            e.employee.employeeId,
            e.employee.fullName,
            e.employmentType,
            e.domain.name,
            e.joiningDate
        )
        FROM EmploymentDetails e
        ORDER BY e.joiningDate DESC
    """)
    List<RecentEmployeeResponse> findRecentlyJoinedEmployees(Pageable pageable);

    @Query("""
        SELECT new com.hirenest.dto.RecentRecordResponse(
            e.employee.id,
            e.employee.employeeId,
            e.employee.fullName,
            'Employment',
            e.joiningDate
        )
        FROM EmploymentDetails e
        ORDER BY e.joiningDate DESC
    """)
    List<RecentRecordResponse> findRecentRecords(Pageable pageable);
}