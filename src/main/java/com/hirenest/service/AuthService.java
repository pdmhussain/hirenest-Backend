
package com.hirenest.service;

import com.hirenest.dto.*;
import com.hirenest.entity.OtpVerification;
import com.hirenest.entity.User;
import com.hirenest.repository.OtpVerificationRepository;
import com.hirenest.repository.UserRepository;
import com.hirenest.security.JwtService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

@Service
public class AuthService {

    private static final String FORGOT_PASSWORD = "FORGOT_PASSWORD";

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final OtpVerificationRepository otpRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final int otpExpirationMinutes;
    private final int maxOtpAttempts;

    private final SecureRandom secureRandom = new SecureRandom();

    public AuthService(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            OtpVerificationRepository otpRepository,
            JwtService jwtService,
            PasswordEncoder passwordEncoder,
            EmailService emailService,
            @Value("${app.otp.expiration-minutes:5}") int otpExpirationMinutes,
            @Value("${app.otp.max-attempts:5}") int maxOtpAttempts) {

        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.otpRepository = otpRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.otpExpirationMinutes = otpExpirationMinutes;
        this.maxOtpAttempts = maxOtpAttempts;
    }

    public LoginResponse login(LoginRequest request) {

        String email = normalize(request.getEmail());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        String token = jwtService.generateToken(user.getEmail());

        return new LoginResponse(
                token,
                user.getEmail(),
                user.getFullName(),
                user.getRole(),
                user.isMustChangePassword()
        );
    }

    @Transactional
    public MessageResponse changePassword(
            String authenticatedEmail,
            ChangePasswordRequest request) {

        String email = normalize(authenticatedEmail);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPassword())) {

            throw new IllegalArgumentException(
                    "Current password is incorrect");
        }

        String newPassword = request.getNewPassword();

        if (newPassword == null || newPassword.length() < 8) {
            throw new IllegalArgumentException(
                    "New password must contain at least 8 characters");
        }

        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new IllegalArgumentException(
                    "New password must differ from the current password");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setMustChangePassword(false);

        userRepository.save(user);

        return new MessageResponse(
                "Password changed successfully. You can now access HireNest.");
    }

    @Transactional
    public MessageResponse createHrAccount(CreateAccountRequest request) {
        return createManagedAccount(request, "HR");
    }

    @Transactional
    public MessageResponse createAdminAccount(CreateAccountRequest request) {
        return createManagedAccount(request, "ADMIN");
    }

    @Transactional
    public MessageResponse createCandidateAccountByAdmin(
            CreateAccountRequest request) {
        return createManagedAccount(request, "USER");
    }

    @Transactional
    public MessageResponse createCandidateAccount(
            CreateAccountRequest request) {
        return createManagedAccount(request, "USER");
    }

    private MessageResponse createManagedAccount(
            CreateAccountRequest request,
            String role) {

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (request.getUsername() == null
                || request.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }

        if (request.getFullName() == null
                || request.getFullName().isBlank()) {
            throw new IllegalArgumentException("Full name is required");
        }

        String email = normalize(request.getEmail());
        String username = request.getUsername().trim();
        String fullName = request.getFullName().trim();

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "An account with this email already exists");
        }

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException(
                    "An account with this username already exists");
        }

        String temporaryPassword = generateTemporaryPassword();

        User user = new User(
                username,
                fullName,
                email,
                passwordEncoder.encode(temporaryPassword),
                role
        );

        user.setActive(true);
        user.setEmailVerified(true);
        user.setMustChangePassword(true);

        userRepository.save(user);

        emailService.sendTemporaryPassword(
                email,
                username,
                temporaryPassword
        );

        String message = switch (role) {
            case "ADMIN" ->
                    "Admin account created. Temporary login details have been emailed.";
            case "HR" ->
                    "HR account created. Temporary login details have been emailed.";
            default ->
                    "Candidate account created. Temporary login details have been emailed.";
        };

        return new MessageResponse(message);
    }

    @Transactional
    public MessageResponse forgotPassword(ForgotPasswordRequest request) {

        String email = normalize(request.getEmail());

        User user = userRepository.findByEmail(email).orElse(null);

        if (user != null) {
            otpRepository.deleteByEmailAndPurposeAndUsedFalse(
                    email, FORGOT_PASSWORD);

            String otp = generateOtp();

            OtpVerification record = new OtpVerification();
            record.setEmail(email);
            record.setOtpHash(passwordEncoder.encode(otp));
            record.setPurpose(FORGOT_PASSWORD);
            record.setExpiresAt(
                    LocalDateTime.now().plusMinutes(otpExpirationMinutes));
            record.setAttempts(0);
            record.setUsed(false);

            otpRepository.save(record);

            emailService.sendOtp(email, "password reset", otp);
        }

        return new MessageResponse(
                "If the email is registered, a password reset OTP has been sent.");
    }

    @Transactional
    public OtpResponse verifyForgotPasswordOtp(
            VerifyForgotOtpRequest request) {

        String email = normalize(request.getEmail());

        OtpVerification record = otpRepository
                .findTopByEmailAndPurposeAndUsedFalseOrderByIdDesc(
                        email, FORGOT_PASSWORD)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Password reset OTP not found or already used"));

        validateOtp(record, request.getOtp());

        record.setUsed(true);
        record.setResetToken(UUID.randomUUID().toString());
        record.setExpiresAt(LocalDateTime.now().plusMinutes(10));

        otpRepository.save(record);

        return new OtpResponse(
                "OTP verified. Use the reset token to set a new password.",
                record.getResetToken());
    }

    @Transactional
    public MessageResponse resetPassword(ResetPasswordRequest request) {

        String email = normalize(request.getEmail());

        OtpVerification record = otpRepository
                .findTopByEmailAndPurposeAndResetTokenAndUsedTrueAndResetCompletedFalseOrderByIdDesc(
                        email,
                        FORGOT_PASSWORD,
                        request.getResetToken())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid or expired reset token"));

        if (record.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    "Reset token has expired");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        String newPassword = request.getNewPassword();

        if (newPassword == null || newPassword.length() < 8) {
            throw new IllegalArgumentException(
                    "New password must contain at least 8 characters");
        }

        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new IllegalArgumentException(
                    "New password must differ from the current password");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setMustChangePassword(false);

        userRepository.save(user);

        record.setResetCompleted(true);
        otpRepository.save(record);

        return new MessageResponse(
                "Password changed successfully. You can now login.");
    }

    private void validateOtp(
            OtpVerification record,
            String rawOtp) {

        if (record.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("OTP has expired");
        }

        if (record.getAttempts() >= maxOtpAttempts) {
            throw new IllegalArgumentException(
                    "Maximum OTP attempts exceeded. Request a new OTP.");
        }

        if (rawOtp == null
                || !passwordEncoder.matches(rawOtp, record.getOtpHash())) {

            record.setAttempts(record.getAttempts() + 1);
            otpRepository.save(record);

            throw new IllegalArgumentException("Invalid OTP");
        }
    }

    private String generateOtp() {
        return String.format(
                "%06d", secureRandom.nextInt(1_000_000));
    }

    private String generateTemporaryPassword() {
        byte[] bytes = new byte[24];
        secureRandom.nextBytes(bytes);
        return java.util.HexFormat.of().formatHex(bytes);
    }

    private String normalize(String email) {
        return email == null
                ? null
                : email.trim().toLowerCase(Locale.ROOT);
    }
}
