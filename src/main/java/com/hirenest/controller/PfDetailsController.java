package com.hirenest.controller;

import com.hirenest.dto.PfDetailsDto;
import com.hirenest.entity.PFDetails;
import com.hirenest.service.PfDetailsService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pf")
@RequiredArgsConstructor
public class PfDetailsController {

    private final PfDetailsService service;


    // =====================================================
    // EMPLOYEE ONLY
    // =====================================================

    @PostMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<PFDetails> createPfDetails(
            @RequestBody PfDetailsDto request,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                service.createPfDetails(request, email)
        );
    }


    // =====================================================
    // HR + ADMIN ONLY
    // =====================================================

    @GetMapping
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public ResponseEntity<List<PFDetails>> getAllPfDetails() {

        return ResponseEntity.ok(
                service.getAllPfDetails()
        );
    }


    // =====================================================
    // HR + ADMIN ONLY
    // =====================================================

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public ResponseEntity<PFDetails> getByEmployeeId(
            @PathVariable Long employeeId) {

        return ResponseEntity.ok(
                service.getByEmployeeId(employeeId)
        );
    }
}