package com.hirenest.controller;

import com.hirenest.dto.BankDetailsDto;
import com.hirenest.entity.BankDetails;
import com.hirenest.service.BankDetailsService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bank")
@RequiredArgsConstructor
public class BankDetailsController {

    private final BankDetailsService service;


    // =====================================================
    // EMPLOYEE ONLY
    // =====================================================

    @PostMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<BankDetails> createBankDetails(
            @RequestBody BankDetailsDto request,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                service.createBankDetails(request, email)
        );
    }


    // =====================================================
    // HR + ADMIN ONLY
    // =====================================================

    @GetMapping
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public ResponseEntity<List<BankDetails>> getAllBankDetails() {

        return ResponseEntity.ok(
                service.getAllBankDetails()
        );
    }


    // =====================================================
    // HR + ADMIN ONLY
    // =====================================================

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public ResponseEntity<BankDetails> getByEmployeeId(
            @PathVariable Long employeeId) {

        return ResponseEntity.ok(
                service.getByEmployeeId(employeeId)
        );
    }
}