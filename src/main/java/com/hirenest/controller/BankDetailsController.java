package com.hirenest.controller;

import com.hirenest.dto.BankDetailsDto;
import com.hirenest.entity.BankDetails;
import com.hirenest.service.BankDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bank")
@RequiredArgsConstructor
public class BankDetailsController {


    private final BankDetailsService service;

    @PostMapping
    public ResponseEntity<BankDetails> createBankDetails(
            @RequestBody BankDetailsDto request) {

        return ResponseEntity.ok(
                service.createBankDetails(request)
        );
    }

    @GetMapping
    public ResponseEntity<List<BankDetails>> getAllBankDetails() {

        return ResponseEntity.ok(
                service.getAllBankDetails()
        );
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<BankDetails> getByEmployeeId(
            @PathVariable Long employeeId) {

        return ResponseEntity.ok(
                service.getByEmployeeId(employeeId)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBankDetails(
            @PathVariable Long id) {

        service.deleteBankDetails(id);

        return ResponseEntity.noContent().build();
    }
}