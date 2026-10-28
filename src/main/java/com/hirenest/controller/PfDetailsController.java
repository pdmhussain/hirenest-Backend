package com.hirenest.controller;

import com.hirenest.dto.PfDetailsDto;
import com.hirenest.entity.PFDetails;
import com.hirenest.service.PfDetailsService;

import lombok.RequiredArgsConstructor;

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

    // ADMIN + HR - CREATE PF DETAILS
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<PFDetails> createPfDetails(
            @RequestBody PfDetailsDto request,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                service.createPfDetails(request, email)
        );
    }

    // ADMIN + HR + USER - GET ALL PF DETAILS
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'USER')")
    public ResponseEntity<List<PFDetails>> getAllPfDetails() {

        return ResponseEntity.ok(
                service.getAllPfDetails()
        );
    }

    // ADMIN + HR + USER - GET PF DETAILS BY EMPLOYEE ID
    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'USER')")
    public ResponseEntity<PFDetails> getByEmployeeId(
            @PathVariable Long employeeId) {

        return ResponseEntity.ok(
                service.getByEmployeeId(employeeId)
        );
    }

    // ADMIN + HR - UPDATE PF DETAILS
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<PFDetails> updatePfDetails(
            @PathVariable Long id,
            @RequestBody PfDetailsDto request) {

        return ResponseEntity.ok(
                service.updatePfDetails(id, request)
        );
    }

    // ADMIN ONLY - DELETE PF DETAILS
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePfDetails(
            @PathVariable Long id) {

        service.deletePfDetails(id);

        return ResponseEntity.noContent().build();
    }
}