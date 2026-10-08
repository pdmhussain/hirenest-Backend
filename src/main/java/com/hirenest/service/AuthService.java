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

import java.time.LocalDateTime;
import java.util.UUID;
import java.security.SecureRandom;

@Service
public class AuthService {
    private static final String REGISTRATION = "REGISTRATION";
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

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository,
                       OtpVerificationRepository otpRepository, JwtService jwtService,
                       PasswordEncoder passwordEncoder, EmailService emailService,
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

    @Transactional
    public MessageResponse register(RegisterRequest request) {
        String email = normalize(request.getEmail());
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email is already registered");
        }

        otpRepository.deleteByEmailAndPurposeAndUsedFalse(email, REGISTRATION);
        String otp = generateOtp();

        OtpVerification record = new OtpVerification();
        record.setEmail(email);
        record.setOtpHash(passwordEncoder.encode(otp));
        record.setPurpose(REGISTRATION);
        record.setPendingPasswordHash(passwordEncoder.encode(request.getPassword()));
        record.setPendingRole("HR");
        record.setExpiresAt(LocalDateTime.now().plusMinutes(otpExpirationMinutes));
        record.setAttempts(0);
        record.setUsed(false);
        otpRepository.save(record);

        emailService.sendOtp(email, "new account registration", otp);
        return new MessageResponse("OTP sent to your email. Verify the OTP to complete registration.");
    }

    @Transactional
    public MessageResponse verifyRegistration(VerifyRegistrationOtpRequest request) {
        String email = normalize(request.getEmail());
        OtpVerification record = otpRepository
                .findTopByEmailAndPurposeAndUsedFalseOrderByIdDesc(email, REGISTRATION)
                .orElseThrow(() -> new IllegalArgumentException("Registration OTP not found or already used"));

        validateOtp(record, request.getOtp());
        record.setUsed(true);
        otpRepository.save(record);

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email is already registered");
        }

        User user = new User(email, record.getPendingPasswordHash(),
                record.getPendingRole() == null ? "HR" : record.getPendingRole());
        user.setEmailVerified(true);
        userRepository.save(user);
        return new MessageResponse("Registration successful. You can now login.");
    }

    public LoginResponse login(LoginRequest request) {
        String email = normalize(request.getEmail());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.getPassword()));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        String token = jwtService.generateToken(user.getEmail());
        return new LoginResponse(token, user.getEmail(), user.getRole());
    }

    @Transactional
    public MessageResponse forgotPassword(ForgotPasswordRequest request) {
        String email = normalize(request.getEmail());
        // Do not reveal whether an account exists.
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            otpRepository.deleteByEmailAndPurposeAndUsedFalse(email, FORGOT_PASSWORD);
            String otp = generateOtp();
            OtpVerification record = new OtpVerification();
            record.setEmail(email);
            record.setOtpHash(passwordEncoder.encode(otp));
            record.setPurpose(FORGOT_PASSWORD);
            record.setExpiresAt(LocalDateTime.now().plusMinutes(otpExpirationMinutes));
            record.setAttempts(0);
            record.setUsed(false);
            otpRepository.save(record);
            emailService.sendOtp(email, "password reset", otp);
        }
        return new MessageResponse("If the email is registered, a password reset OTP has been sent.");
    }

    @Transactional
    public OtpResponse verifyForgotPasswordOtp(VerifyForgotOtpRequest request) {
        String email = normalize(request.getEmail());
        OtpVerification record = otpRepository
                .findTopByEmailAndPurposeAndUsedFalseOrderByIdDesc(email, FORGOT_PASSWORD)
                .orElseThrow(() -> new IllegalArgumentException("Password reset OTP not found or already used"));

        validateOtp(record, request.getOtp());
        record.setUsed(true);
        record.setResetToken(UUID.randomUUID().toString());
        record.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        otpRepository.save(record);
        return new OtpResponse("OTP verified. Use the reset token to set a new password.", record.getResetToken());
    }

    @Transactional
    public MessageResponse resetPassword(ResetPasswordRequest request) {
        String email = normalize(request.getEmail());
        OtpVerification record = otpRepository
                .findTopByEmailAndPurposeAndResetTokenAndUsedTrueAndResetCompletedFalseOrderByIdDesc(
                        email, FORGOT_PASSWORD, request.getResetToken())
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired reset token"));

        if (record.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Reset token has expired");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setEmailVerified(true);
        userRepository.save(user);

        record.setResetCompleted(true);
        otpRepository.save(record);
        return new MessageResponse("Password changed successfully. You can now login.");
    }

    private void validateOtp(OtpVerification record, String rawOtp) {
        if (record.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("OTP has expired");
        }
        if (record.getAttempts() >= maxOtpAttempts) {
            throw new IllegalArgumentException("Maximum OTP attempts exceeded. Request a new OTP.");
        }
        if (!passwordEncoder.matches(rawOtp, record.getOtpHash())) {
            record.setAttempts(record.getAttempts() + 1);
            otpRepository.save(record);
            throw new IllegalArgumentException("Invalid OTP");
        }
    }

    private String generateOtp() {
        return String.format("%06d", secureRandom.nextInt(1_000_000));
    }

    private String normalize(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }
}
