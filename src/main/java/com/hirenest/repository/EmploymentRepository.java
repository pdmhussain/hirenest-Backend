package com.hirenest.repository;

import com.hirenest.entity.EmploymentDetails;
import com.hirenest.enums.Domain;
import com.hirenest.enums.EmploymentStatus;
import com.hirenest.enums.EmploymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmploymentRepository extends JpaRepository<EmploymentDetails, Long> {

    Optional<EmploymentDetails> findByEmployeeId(Long employeeId);

    boolean existsByEmployeeId(Long employeeId);

    List<EmploymentDetails> findByDomain(Domain domain);

    List<EmploymentDetails> findByEmploymentType(EmploymentType employmentType);

    List<EmploymentDetails> findByStatus(EmploymentStatus status);
}
