package com.hirenest.repository;

import com.hirenest.entity.OfferLetter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OfferLetterRepository
        extends JpaRepository<OfferLetter, Long> {

    Optional<OfferLetter> findByEmployeeId(Long employeeId);

    Optional<OfferLetter> findByOfferLetterNumber(
            String offerLetterNumber
    );
}