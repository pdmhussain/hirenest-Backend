package com.hirenest.repository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.hirenest.entity.ProbationDetails;

public interface ProbationRepository
        extends JpaRepository<ProbationDetails, Long> {

    boolean existsByEmployeeEmployeeId(String employeeId);

    Optional<ProbationDetails> findByEmployeeEmployeeId(
            String employeeId);

    List<ProbationDetails> findByEmployee_Domain_Id(Long domainId);

    @Query("""
        SELECT COUNT(e.id)
        FROM Employee e
        WHERE e.domain.id = :domainId
          AND e.status = 'ACTIVE'
        """)
    long countActiveEmployeesByDomain(
            @Param("domainId") Long domainId);

    @Query("""
        SELECT COUNT(p.id)
        FROM ProbationDetails p
        WHERE p.employee.domain.id = :domainId
          AND p.employee.status = 'ACTIVE'
          AND p.probationEndDate < CURRENT_DATE
        """)
    long countCompletedProbationsByDomain(
            @Param("domainId") Long domainId);

    @Query("""
        SELECT COUNT(p.id)
        FROM ProbationDetails p
        WHERE p.employee.domain.id = :domainId
          AND p.employee.status = 'ACTIVE'
          AND p.probationStartDate <= CURRENT_DATE
          AND p.probationEndDate >= CURRENT_DATE
        """)
    long countInProgressProbationsByDomain(
            @Param("domainId") Long domainId);
}
