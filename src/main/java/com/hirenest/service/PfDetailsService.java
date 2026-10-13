package com.hirenest.service;

import com.hirenest.dto.PfDetailsDto;
import com.hirenest.entity.PFDetails;

import java.util.List;

public interface PfDetailsService {


    public PFDetails createPfDetails(
            PfDetailsDto req,
            String email);

    List<PFDetails> getAllPfDetails();

    PFDetails getByEmployeeId(Long employeeId);


    void deletePfDetails(Long id);
}
