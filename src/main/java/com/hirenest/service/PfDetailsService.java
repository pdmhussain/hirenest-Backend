package com.hirenest.service;

import com.hirenest.dto.PfDetailsDto;
import com.hirenest.entity.PFDetails;

import java.util.List;

public interface PfDetailsService {

    PFDetails createPfDetails(
            PfDetailsDto request,
            String email
    );

    List<PFDetails> getAllPfDetails();

    PFDetails getByEmployeeId(Long employeeId);

    PFDetails updatePfDetails(
            Long id,
            PfDetailsDto request
    );

    void deletePfDetails(Long id);
}