package com.hirenest.repository;

import com.hirenest.entity.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Long> {
    Optional<OtpVerification> findTopByEmailAndPurposeAndUsedFalseOrderByIdDesc(String email, String purpose);
    void deleteByEmailAndPurposeAndUsedFalse(String email, String purpose);
    Optional<OtpVerification> findTopByEmailAndPurposeAndResetTokenAndUsedTrueAndResetCompletedFalseOrderByIdDesc(
            String email, String purpose, String resetToken);
}
