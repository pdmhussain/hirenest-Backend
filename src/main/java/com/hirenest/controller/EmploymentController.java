package com.hirenest.controller;

import com.hirenest.dto.EmploymentRequest;
import com.hirenest.dto.EmploymentResponse;
import com.hirenest.enums.Domain;
import com.hirenest.enums.EmploymentStatus;
import com.hirenest.enums.EmploymentType;
import com.hirenest.service.EmploymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/employment")
@CrossOrigin(origins = "http://localhost:5173")
public class EmploymentController {

    private final EmploymentService service;

    public EmploymentController(EmploymentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<EmploymentResponse> create(@Valid @RequestBody EmploymentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping
    public ResponseEntity<List<EmploymentResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmploymentResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<EmploymentResponse> getByEmployeeId(@PathVariable Long employeeId) {
        return ResponseEntity.ok(service.getByEmployeeId(employeeId));
    }

  
    @GetMapping("/domains")
    public ResponseEntity<List<String>> getDomains() {
        return ResponseEntity.ok(Arrays.stream(Domain.values()).map(Domain::getDisplayName).toList());
    }

      @GetMapping("/domain/{domain}")
    public ResponseEntity<List<EmploymentResponse>> getByDomain(@PathVariable String domain) {
        return ResponseEntity.ok(service.getByDomain(Domain.fromValue(domain)));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<EmploymentResponse>> getByType(@PathVariable EmploymentType type) {
        return ResponseEntity.ok(service.getByType(type));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<EmploymentResponse>> getByStatus(@PathVariable EmploymentStatus status) {
        return ResponseEntity.ok(service.getByStatus(status));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmploymentResponse> update(@PathVariable Long id,
                                                     @Valid @RequestBody EmploymentRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<EmploymentResponse> updateStatus(@PathVariable Long id,
                                                           @RequestParam EmploymentStatus status) {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
