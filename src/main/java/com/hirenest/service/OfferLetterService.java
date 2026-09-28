package com.hirenest.service;

import com.hirenest.dto.OfferLetterRequest;
import com.hirenest.dto.OfferLetterResponse;

import java.util.List;

public interface OfferLetterService {

    OfferLetterResponse createOfferLetter(OfferLetterRequest request);

    OfferLetterResponse getOfferLetterById(Long id);

    List<OfferLetterResponse> getAllOfferLetters();

    OfferLetterResponse getOfferLetterByEmployeeId(Long employeeId);

    OfferLetterResponse updateOfferLetter(Long id, OfferLetterRequest request);

    void deleteOfferLetter(Long id);
}