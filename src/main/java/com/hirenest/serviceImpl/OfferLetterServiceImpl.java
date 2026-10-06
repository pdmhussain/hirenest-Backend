package com.hirenest.serviceImpl;

import com.hirenest.dto.OfferLetterRequest;
import com.hirenest.dto.OfferLetterResponse;
import com.hirenest.entity.OfferLetter;
import com.hirenest.exception.BadRequestException;
import com.hirenest.exception.DuplicateResourceException;
import com.hirenest.exception.ResourceNotFoundException;
import com.hirenest.repository.OfferLetterRepository;
import com.hirenest.service.OfferLetterService;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class OfferLetterServiceImpl implements OfferLetterService {

    private final OfferLetterRepository offerLetterRepository;

    public OfferLetterServiceImpl(
            OfferLetterRepository offerLetterRepository) {

        this.offerLetterRepository = offerLetterRepository;
    }


    @Override
    public OfferLetterResponse createOfferLetter(
            OfferLetterRequest request) {

        // Check duplicate offer letter number
        if (offerLetterRepository
                .findByOfferLetterNumber(
                        request.getOfferLetterNumber())
                .isPresent()) {

            throw new DuplicateResourceException(
                    "Offer letter number already exists: "
                            + request.getOfferLetterNumber()
            );
        }

        OfferLetter offerLetter = new OfferLetter();

        mapRequestToEntity(request, offerLetter);

        OfferLetter savedOfferLetter =
                offerLetterRepository.save(offerLetter);

        return mapToResponse(savedOfferLetter);
    }

    @Override
    public OfferLetterResponse getOfferLetterById(Long id) {

        OfferLetter offerLetter =
                getOfferLetterEntityById(id);

        return mapToResponse(offerLetter);
    }



    @Override
    public List<OfferLetterResponse> getAllOfferLetters() {

        return offerLetterRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    @Override
    public OfferLetterResponse getOfferLetterByEmployeeId(
            Long employeeId) {

        OfferLetter offerLetter =
                offerLetterRepository
                        .findByEmployeeId(employeeId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Offer letter not found for employee: "
                                                + employeeId
                                )
                        );

        return mapToResponse(offerLetter);
    }


    @Override
    public OfferLetterResponse updateOfferLetter(
            Long id,
            OfferLetterRequest request) {

        OfferLetter offerLetter =
                getOfferLetterEntityById(id);


        offerLetterRepository
                .findByOfferLetterNumber(
                        request.getOfferLetterNumber())
                .ifPresent(existingOfferLetter -> {

                    if (!existingOfferLetter.getId()
                            .equals(id)) {

                        throw new DuplicateResourceException(
                                "Offer letter number already exists: "
                                        + request.getOfferLetterNumber()
                        );
                    }
                });



        offerLetter.setEmployeeId(
                request.getEmployeeId()
        );

        offerLetter.setOfferLetterNumber(
                request.getOfferLetterNumber()
        );

        offerLetter.setOfferDate(
                request.getOfferDate()
        );

        offerLetter.setEmploymentType(
                request.getEmploymentType()
        );

        offerLetter.setOfferedSalary(
                request.getOfferedSalary()
        );

        offerLetter.setOfferedStipend(
                request.getOfferedStipend()
        );

        offerLetter.setJoiningDate(
                request.getJoiningDate()
        );

        offerLetter.setOfferStatus(
                request.getOfferStatus()
        );


        MultipartFile file =
                request.getOfferLetterFile();

        if (file != null && !file.isEmpty()) {

            validatePdf(file);

            try {

                offerLetter.setOfferLetterFile(
                        file.getBytes()
                );

                offerLetter.setFileName(
                        file.getOriginalFilename()
                );

                offerLetter.setContentType(
                        file.getContentType()
                );

            } catch (IOException e) {

                throw new BadRequestException(
                        "Failed to upload offer letter file"
                );
            }
        }


        OfferLetter updatedOfferLetter =
                offerLetterRepository.save(offerLetter);

        return mapToResponse(updatedOfferLetter);
    }



    @Override
    public void deleteOfferLetter(Long id) {

        OfferLetter offerLetter =
                getOfferLetterEntityById(id);

        offerLetterRepository.delete(offerLetter);
    }



    @Override
    public OfferLetter getOfferLetterEntityById(Long id) {

        return offerLetterRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Offer letter not found with id: "
                                        + id
                        )
                );
    }



    private void mapRequestToEntity(
            OfferLetterRequest request,
            OfferLetter offerLetter) {


        offerLetter.setEmployeeId(
                request.getEmployeeId()
        );

        offerLetter.setOfferLetterNumber(
                request.getOfferLetterNumber()
        );

        offerLetter.setOfferDate(
                request.getOfferDate()
        );

        offerLetter.setEmploymentType(
                request.getEmploymentType()
        );

        offerLetter.setOfferedSalary(
                request.getOfferedSalary()
        );

        offerLetter.setOfferedStipend(
                request.getOfferedStipend()
        );

        offerLetter.setJoiningDate(
                request.getJoiningDate()
        );

        offerLetter.setOfferStatus(
                request.getOfferStatus()
        );


        MultipartFile file =
                request.getOfferLetterFile();

        if (file != null && !file.isEmpty()) {

            validatePdf(file);

            try {

                offerLetter.setOfferLetterFile(
                        file.getBytes()
                );

                offerLetter.setFileName(
                        file.getOriginalFilename()
                );

                offerLetter.setContentType(
                        file.getContentType()
                );

            } catch (IOException e) {

                throw new BadRequestException(
                        "Failed to upload offer letter file"
                );
            }
        }
    }


    private void validatePdf(MultipartFile file) {

        String contentType =
                file.getContentType();

        String fileName =
                file.getOriginalFilename();


        boolean validContentType =
                "application/pdf".equalsIgnoreCase(
                        contentType
                );


        boolean validExtension =
                fileName != null
                        && fileName
                        .toLowerCase()
                        .endsWith(".pdf");


        if (!validContentType || !validExtension) {

            throw new BadRequestException(
                    "Only PDF files are allowed"
            );
        }
    }



    private OfferLetterResponse mapToResponse(
            OfferLetter offerLetter) {

        OfferLetterResponse response =
                new OfferLetterResponse();


        response.setId(
                offerLetter.getId()
        );

        response.setEmployeeId(
                offerLetter.getEmployeeId()
        );

        response.setOfferLetterNumber(
                offerLetter.getOfferLetterNumber()
        );

        response.setOfferDate(
                offerLetter.getOfferDate()
        );


        // File metadata only
        // Actual PDF is NOT returned in JSON

        response.setFileName(
                offerLetter.getFileName()
        );

        response.setContentType(
                offerLetter.getContentType()
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