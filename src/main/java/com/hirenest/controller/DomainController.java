package com.hirenest.controller;

import com.hirenest.dto.DomainRequest;
import com.hirenest.entity.Domain;
import com.hirenest.service.DomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/domains")
@RequiredArgsConstructor
public class DomainController {

    private final DomainService domainService;

    // ADMIN + HR can create Domain
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    @PostMapping
    public ResponseEntity<Domain> createDomain(
            @RequestBody DomainRequest request) {

        return new ResponseEntity<>(
                domainService.createDomain(request),
                HttpStatus.CREATED
        );
    }

    // ADMIN + HR + USER can view one Domain
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'USER')")
    @GetMapping("/{id}")
    public ResponseEntity<Domain> getDomainById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                domainService.getDomainById(id)
        );
    }

    // ADMIN + HR + USER can view all Domains
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'USER')")
    @GetMapping
    public ResponseEntity<List<Domain>> getAllDomains() {

        return ResponseEntity.ok(
                domainService.getAllDomains()
        );
    }

    // ADMIN + HR can update Domain
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    @PutMapping("/{id}")
    public ResponseEntity<Domain> updateDomain(
            @PathVariable Long id,
            @RequestBody DomainRequest request) {

        return ResponseEntity.ok(
                domainService.updateDomain(id, request)
        );
    }

    // Only ADMIN can delete Domain
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDomain(
            @PathVariable Long id) {

        domainService.deleteDomain(id);

        return ResponseEntity.noContent().build();
    }
}