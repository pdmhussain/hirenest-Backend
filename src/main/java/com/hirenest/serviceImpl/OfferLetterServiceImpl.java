package com.hirenest.serviceImpl;

import com.hirenest.dto.OfferLetterRequest;
import com.hirenest.dto.OfferLetterResponse;
import com.hirenest.entity.OfferLetter;
import com.hirenest.repository.OfferLetterRepository;
import com.hirenest.service.OfferLetterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OfferLetterServiceImpl implements OfferLetterService {

    private final OfferLetterRepository offerLetterRepository;

    @Override
    public OfferLetterResponse createOfferLetter(OfferLetterRequest request) {

        OfferLetter offerLetter = new OfferLetter();

        offerLetter.setEmployeeId(request.getEmployeeId());
        offerLetter.setOfferLetterNumber(request.getOfferLetterNumber());
        offerLetter.setOfferDate(request.getOfferDate());
        offerLetter.setOfferLetterFile(request.getOfferLetterFile());
        offerLetter.setEmploymentType(request.getEmploymentType());
        offerLetter.setOfferedSalary(request.getOfferedSalary());
        offerLetter.setOfferedStipend(request.getOfferedStipend());
        offerLetter.setJoiningDate(request.getJoiningDate());
        offerLetter.setOfferStatus(request.getOfferStatus());

        OfferLetter savedOfferLetter =
                offerLetterRepository.save(offerLetter);

        return convertToResponse(savedOfferLetter);
    }

    @Override
    public OfferLetterResponse getOfferLetterById(Long id) {

        OfferLetter offerLetter = offerLetterRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Offer letter not found with id: " + id)
                );

        return convertToResponse(offerLetter);
    }

    @Override
    public List<OfferLetterResponse> getAllOfferLetters() {

        return offerLetterRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    public OfferLetterResponse getOfferLetterByEmployeeId(Long employeeId) {

        OfferLetter offerLetter =
                offerLetterRepository.findByEmployeeId(employeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Offer letter not found for employee: "
                                                + employeeId
                                )
                        );

        return convertToResponse(offerLetter);
    }

    @Override
    public OfferLetterResponse updateOfferLetter(
            Long id,
            OfferLetterRequest request) {

        OfferLetter offerLetter = offerLetterRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Offer letter not found with id: " + id)
                );

        offerLetter.setEmployeeId(request.getEmployeeId());
        offerLetter.setOfferLetterNumber(request.getOfferLetterNumber());
        offerLetter.setOfferDate(request.getOfferDate());
        offerLetter.setOfferLetterFile(request.getOfferLetterFile());
        offerLetter.setEmploymentType(request.getEmploymentType());
        offerLetter.setOfferedSalary(request.getOfferedSalary());
        offerLetter.setOfferedStipend(request.getOfferedStipend());
        offerLetter.setJoiningDate(request.getJoiningDate());
        offerLetter.setOfferStatus(request.getOfferStatus());

        OfferLetter updatedOfferLetter =
                offerLetterRepository.save(offerLetter);

        return convertToResponse(updatedOfferLetter);
    }

    @Override
    public void deleteOfferLetter(Long id) {

        OfferLetter offerLetter = offerLetterRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Offer letter not found with id: " + id)
                );

        offerLetterRepository.delete(offerLetter);
    }

    private OfferLetterResponse convertToResponse(
            OfferLetter offerLetter) {

        OfferLetterResponse response = new OfferLetterResponse();

        response.setId(offerLetter.getId());
        response.setEmployeeId(offerLetter.getEmployeeId());
        response.setOfferLetterNumber(
                offerLetter.getOfferLetterNumber()
        );
        response.setOfferDate(offerLetter.getOfferDate());
        response.setOfferLetterFile(
                offerLetter.getOfferLetterFile()
        );
        response.setEmploymentType(
                offerLetter.getEmploymentType()
        );
        response.setOfferedSalary(
                offerLetter.getOfferedSalary()
        );
        response.setOfferedStipend(
                offerLetter.getOfferedStipend()
        );
        response.setJoiningDate(
                offerLetter.getJoiningDate()
        );
        response.setOfferStatus(
                offerLetter.getOfferStatus()
        );

        return response;
    }
}