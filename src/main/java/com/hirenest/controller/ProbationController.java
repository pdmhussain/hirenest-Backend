package com.hirenest.controller;

import com.hirenest.dto.ProbationRequest;
import com.hirenest.entity.ProbationDetails;
import com.hirenest.service.ProbationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/probations")
@RequiredArgsConstructor
public class ProbationController {

    private final ProbationService probationService;

    // ADMIN + HR can create Probation
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    @PostMapping
    public ResponseEntity<ProbationDetails> createProbation(
            @RequestBody ProbationRequest request) {

        return new ResponseEntity<>(
                probationService.createProbation(request),
                HttpStatus.CREATED
        );
    }

    // ADMIN + HR + USER can view one Probation
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'USER')")
    @GetMapping("/{id}")
    public ResponseEntity<ProbationDetails> getProbationById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                probationService.getProbationById(id)
        );
    }

    // ADMIN + HR + USER can view all Probations
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'USER')")
    @GetMapping
    public ResponseEntity<List<ProbationDetails>> getAllProbations() {

        return ResponseEntity.ok(
                probationService.getAllProbations()
        );
    }

    // ADMIN + HR can update Probation
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    @PutMapping("/{id}")
    public ResponseEntity<ProbationDetails> updateProbation(
            @PathVariable Long id,
            @RequestBody ProbationRequest request) {

        return ResponseEntity.ok(
                probationService.updateProbation(id, request)
        );
    }

    // Only ADMIN can delete Probation
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProbation(
            @PathVariable Long id) {

        probationService.deleteProbation(id);

        return ResponseEntity.noContent().build();
    }
} 