package com.hirenest.repository;

import com.hirenest.entity.ProbationDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProbationRepository extends JpaRepository<ProbationDetails, Long> {
}