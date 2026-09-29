package com.hirenest.controller;

import com.hirenest.dto.PfDetailsDto;
import com.hirenest.entity.PFDetails;
import com.hirenest.service.PfDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pf")
@RequiredArgsConstructor
public class PfDetailsController {

    private final PfDetailsService service;

    @PostMapping
    public ResponseEntity<PFDetails> createPfDetails(
            @RequestBody PfDetailsDto request) {

        return ResponseEntity.ok(
                service.createPfDetails(request)
        );
    }

    @GetMapping
    public ResponseEntity<List<PFDetails>> getAllPfDetails() {

        return ResponseEntity.ok(
                service.getAllPfDetails()
        );
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<PFDetails> getByEmployeeId(
            @PathVariable Long employeeId) {

        return ResponseEntity.ok(
                service.getByEmployeeId(employeeId)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePfDetails(
            @PathVariable Long id) {

        service.deletePfDetails(id);

        return ResponseEntity.noContent().build();
    }
}
