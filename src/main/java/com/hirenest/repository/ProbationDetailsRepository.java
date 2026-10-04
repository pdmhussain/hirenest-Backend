package com.hirenest.repository;

import com.hirenest.entity.ProbationDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

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
}