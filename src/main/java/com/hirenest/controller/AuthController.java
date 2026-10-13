
package com.hirenest.controller;

import com.hirenest.dto.*;
import com.hirenest.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {
        "http://localhost:3000",
        "http://localhost:5173"
})
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        return ResponseEntity.ok(
                authService.login(request));
    }

    @PostMapping("/change-password")
    public ResponseEntity<MessageResponse> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ChangePasswordRequest request) {

        return ResponseEntity.ok(
                authService.changePassword(
                        userDetails.getUsername(),
                        request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/create-admin")
    public ResponseEntity<MessageResponse> createAdminAccount(
            @Valid @RequestBody CreateAccountRequest request) {

        return ResponseEntity.ok(
                authService.createAdminAccount(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/create-hr")
    public ResponseEntity<MessageResponse> createHrAccount(
            @Valid @RequestBody CreateAccountRequest request) {

        return ResponseEntity.ok(
                authService.createHrAccount(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/create-candidate")
    public ResponseEntity<MessageResponse> createCandidateAccountByAdmin(
            @Valid @RequestBody CreateAccountRequest request) {

        return ResponseEntity.ok(
                authService.createCandidateAccountByAdmin(request));
    }

    @PreAuthorize("hasRole('HR')")
    @PostMapping("/hr/create-candidate")
    public ResponseEntity<MessageResponse> createCandidateAccount(
            @Valid @RequestBody CreateAccountRequest request) {

        return ResponseEntity.ok(
                authService.createCandidateAccount(request));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<MessageResponse> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {

        return ResponseEntity.ok(
                authService.forgotPassword(request));
    }

    @PostMapping("/verify-forgot-password")
    public ResponseEntity<OtpResponse> verifyForgotPassword(
            @Valid @RequestBody VerifyForgotOtpRequest request) {

        return ResponseEntity.ok(
                authService.verifyForgotPasswordOtp(request));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponse> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {

        return ResponseEntity.ok(
                authService.resetPassword(request));
    }
}

