package com.hirenest.repository;

import com.hirenest.entity.PFDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PfDetailsRepository extends JpaRepository<PFDetails, Long> {
    Optional<PFDetails> findByEmployee_Id(Long employeeId);

}
