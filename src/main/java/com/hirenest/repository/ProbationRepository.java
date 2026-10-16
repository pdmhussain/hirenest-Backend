package com.hirenest.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hirenest.entity.ProbationDetails;

@Repository
public interface ProbationRepository
        extends JpaRepository<ProbationDetails, Long> {

    Optional<ProbationDetails> findByEmployeeEmployeeId(String employeeId);

    boolean existsByEmployeeEmployeeId(String employeeId);
}