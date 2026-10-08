package com.hirenest.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "otp_verifications")
public class OtpVerification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String otpHash;

    @Column(nullable = false)
    private String purpose;

    private String pendingPasswordHash;
    private String pendingRole;
    private String resetToken;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private int attempts = 0;

    @Column(nullable = false)
    private boolean used = false;

    @Column(nullable = false)
    private boolean resetCompleted = false;

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getOtpHash() { return otpHash; }
    public String getPurpose() { return purpose; }
    public String getPendingPasswordHash() { return pendingPasswordHash; }
    public String getPendingRole() { return pendingRole; }
    public String getResetToken() { return resetToken; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public int getAttempts() { return attempts; }
    public boolean isUsed() { return used; }
    public boolean isResetCompleted() { return resetCompleted; }

    public void setId(Long id) { this.id = id; }
    public void setEmail(String email) { this.email = email; }
    public void setOtpHash(String otpHash) { this.otpHash = otpHash; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
    public void setPendingPasswordHash(String pendingPasswordHash) { this.pendingPasswordHash = pendingPasswordHash; }
    public void setPendingRole(String pendingRole) { this.pendingRole = pendingRole; }
    public void setResetToken(String resetToken) { this.resetToken = resetToken; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    public void setAttempts(int attempts) { this.attempts = attempts; }
    public void setUsed(boolean used) { this.used = used; }
    public void setResetCompleted(boolean resetCompleted) { this.resetCompleted = resetCompleted; }
}
