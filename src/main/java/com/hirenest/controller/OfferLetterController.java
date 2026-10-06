package com.hirenest.controller;

import com.hirenest.dto.OfferLetterRequest;
import com.hirenest.dto.OfferLetterResponse;
import com.hirenest.entity.OfferLetter;
import com.hirenest.service.OfferLetterService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offer-letters")
public class OfferLetterController {

    private final OfferLetterService offerLetterService;

    public OfferLetterController(
            OfferLetterService offerLetterService) {

        this.offerLetterService = offerLetterService;
    }

    // CREATE + PDF UPLOAD
    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<OfferLetterResponse>
    createOfferLetter(
            @Valid @ModelAttribute OfferLetterRequest request) {

        return ResponseEntity.ok(
                offerLetterService.createOfferLetter(request)
        );
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<OfferLetterResponse>
    getOfferLetterById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                offerLetterService
                        .getOfferLetterById(id)
        );
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<OfferLetterResponse>>
    getAllOfferLetters() {

        return ResponseEntity.ok(
                offerLetterService
                        .getAllOfferLetters()
        );
    }

    // GET BY EMPLOYEE
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<OfferLetterResponse>
    getOfferLetterByEmployeeId(
            @PathVariable Long employeeId) {

        return ResponseEntity.ok(
                offerLetterService
                        .getOfferLetterByEmployeeId(
                                employeeId
                        )
        );
    }

    // UPDATE + OPTIONAL PDF REPLACEMENT
    @PutMapping(
            value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<OfferLetterResponse>
    updateOfferLetter(
            @PathVariable Long id,
            @Valid @ModelAttribute OfferLetterRequest request) {

        return ResponseEntity.ok(
                offerLetterService.updateOfferLetter(
                        id,
                        request
                )
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String>
    deleteOfferLetter(
            @PathVariable Long id) {

        offerLetterService.deleteOfferLetter(id);

        return ResponseEntity.ok(
                "Offer letter deleted successfully"
        );
    }

    // VIEW PDF IN BROWSER
    @GetMapping("/{id}/view")
    public ResponseEntity<byte[]> viewOfferLetter(
            @PathVariable Long id) {

        OfferLetter offerLetter =
                offerLetterService
                        .getOfferLetterEntityById(id);

        if (offerLetter.getOfferLetterFile() == null) {

            return ResponseEntity.notFound()
                    .build();
        }

        MediaType mediaType =
                getMediaType(
                        offerLetter.getContentType()
                );

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" +
                                offerLetter.getFileName() +
                                "\""
                )
                .body(
                        offerLetter.getOfferLetterFile()
                );
    }

    // DOWNLOAD PDF
    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadOfferLetter(
            @PathVariable Long id) {

        OfferLetter offerLetter =
                offerLetterService
                        .getOfferLetterEntityById(id);

        if (offerLetter.getOfferLetterFile() == null) {

            return ResponseEntity.notFound()
                    .build();
        }

        MediaType mediaType =
                getMediaType(
                        offerLetter.getContentType()
                );

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                offerLetter.getFileName() +
                                "\""
                )
                .body(
                        offerLetter.getOfferLetterFile()
                );
    }

    private MediaType getMediaType(
            String contentType) {

        if (contentType == null ||
                contentType.isBlank()) {

            return MediaType.APPLICATION_PDF;
        }

        try {

            return MediaType.parseMediaType(
                    contentType
            );

        } catch (Exception e) {

            return MediaType.APPLICATION_PDF;
        }
    }
}