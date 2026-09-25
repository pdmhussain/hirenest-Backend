package com.hirenest.service;

import com.hirenest.dto.PfDetailsDto;
import com.hirenest.entity.PFDetails;

import java.util.List;

public interface PfDetailsService {

    public PFDetails createPfDetails(PfDetailsDto request);

    public List<PFDetails> getAllPfDetails();

    public PFDetails getByEmployeeId(Long employeeId);

    public void deletePfDetails(Long id);
}
