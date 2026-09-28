package com.hirenest.controller;

import com.hirenest.dto.OfferLetterRequest;
import com.hirenest.dto.OfferLetterResponse;
import com.hirenest.service.OfferLetterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offer-letters")
@RequiredArgsConstructor
public class OfferLetterController {

    private final OfferLetterService offerLetterService;


    @PostMapping
    public ResponseEntity<OfferLetterResponse> createOfferLetter(
            @Valid @RequestBody OfferLetterRequest request) {

        OfferLetterResponse response =
                offerLetterService.createOfferLetter(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<OfferLetterResponse> getOfferLetterById(
            @PathVariable Long id) {

        OfferLetterResponse response =
                offerLetterService.getOfferLetterById(id);

        return ResponseEntity.ok(response);
    }


    @GetMapping
    public ResponseEntity<List<OfferLetterResponse>> getAllOfferLetters() {

        List<OfferLetterResponse> response =
                offerLetterService.getAllOfferLetters();

        return ResponseEntity.ok(response);
    }


    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<OfferLetterResponse> getOfferLetterByEmployeeId(
            @PathVariable Long employeeId) {

        OfferLetterResponse response =
                offerLetterService.getOfferLetterByEmployeeId(employeeId);

        return ResponseEntity.ok(response);
    }


    @PutMapping("/{id}")
    public ResponseEntity<OfferLetterResponse> updateOfferLetter(
            @PathVariable Long id,
            @Valid @RequestBody OfferLetterRequest request) {

        OfferLetterResponse response =
                offerLetterService.updateOfferLetter(id, request);

        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOfferLetter(
            @PathVariable Long id) {

        offerLetterService.deleteOfferLetter(id);

        return ResponseEntity.noContent().build();
    }
}