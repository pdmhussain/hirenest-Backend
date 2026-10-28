package com.hirenest.controller;

import com.hirenest.dto.BankDetailsDto;
import com.hirenest.entity.BankDetails;
import com.hirenest.service.BankDetailsService;

import lombok.RequiredArgsConstructor;

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

    // ADMIN + HR - CREATE BANK DETAILS
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<BankDetails> createBankDetails(
            @RequestBody BankDetailsDto request,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                service.createBankDetails(request, email)
        );
    }

    // ADMIN + HR + USER - GET ALL BANK DETAILS
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'USER')")
    public ResponseEntity<List<BankDetails>> getAllBankDetails() {

        return ResponseEntity.ok(
                service.getAllBankDetails()
        );
    }

    // ADMIN + HR + USER - GET BANK DETAILS BY EMPLOYEE ID
    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'USER')")
    public ResponseEntity<BankDetails> getByEmployeeId(
            @PathVariable Long employeeId) {

        return ResponseEntity.ok(
                service.getByEmployeeId(employeeId)
        );
    }

    // ADMIN + HR - UPDATE BANK DETAILS
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<BankDetails> updateBankDetails(
            @PathVariable Long id,
            @RequestBody BankDetailsDto request) {

        return ResponseEntity.ok(
                service.updateBankDetails(id, request)
        );
    }

    // ADMIN ONLY - DELETE BANK DETAILS
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBankDetails(
            @PathVariable Long id) {

        service.deleteBankDetails(id);

        return ResponseEntity.noContent().build();
    }
}