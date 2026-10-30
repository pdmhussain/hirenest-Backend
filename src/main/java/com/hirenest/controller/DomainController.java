package com.hirenest.controller;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hirenest.dto.DomainRequest;
import com.hirenest.dto.DomainResponse;
import com.hirenest.service.DomainService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/domains")
@RequiredArgsConstructor
public class DomainController {

    private final DomainService domainService;

    @PostMapping
    public ResponseEntity<DomainResponse> createDomain(
            @RequestBody DomainRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(domainService.createDomain(request));
    }

    @GetMapping
    public ResponseEntity<List<DomainResponse>> getAllDomains() {
        return ResponseEntity.ok(domainService.getAllDomains());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DomainResponse> getDomainById(
            @PathVariable Long id) {

        return ResponseEntity.ok(domainService.getDomainById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DomainResponse> updateDomain(
            @PathVariable Long id,
            @RequestBody DomainRequest request) {

        return ResponseEntity.ok(
                domainService.updateDomain(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDomain(
            @PathVariable Long id) {

        domainService.deleteDomain(id);
        return ResponseEntity.noContent().build();
    }
}
