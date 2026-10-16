package com.hirenest.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hirenest.dto.ProbationRequest;
import com.hirenest.dto.ProbationResponse;
import com.hirenest.service.ProbationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/probations")
@RequiredArgsConstructor
public class ProbationController {

    private final ProbationService probationService;

    @PostMapping
    public ResponseEntity<ProbationResponse> createProbation(
            @RequestBody ProbationRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(probationService.createProbation(request));
    }

    @GetMapping
    public ResponseEntity<List<ProbationResponse>> getAllProbations() {

        return ResponseEntity.ok(
                probationService.getAllProbations());
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ProbationResponse> getProbationByEmployeeId(
            @PathVariable String employeeId) {

        return ResponseEntity.ok(
                probationService.getProbationByEmployeeId(employeeId));
    }

    @PutMapping("/employee/{employeeId}")
    public ResponseEntity<ProbationResponse> updateProbation(
            @PathVariable String employeeId,
            @RequestBody ProbationRequest request) {

        return ResponseEntity.ok(
                probationService.updateProbation(employeeId, request));
    }

    @DeleteMapping("/employee/{employeeId}")
    public ResponseEntity<Void> deleteProbation(
            @PathVariable String employeeId) {

        probationService.deleteProbation(employeeId);
        return ResponseEntity.noContent().build();
    }
}