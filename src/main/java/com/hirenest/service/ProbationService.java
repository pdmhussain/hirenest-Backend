package com.hirenest.service;

import com.hirenest.dto.ProbationRequest;
import com.hirenest.entity.ProbationDetails;

import java.util.List;

public interface ProbationService {

    ProbationDetails createProbation(ProbationRequest request);

    ProbationDetails getProbationById(Long id);

    List<ProbationDetails> getAllProbations();

    ProbationDetails updateProbation(Long id, ProbationRequest request);

    void deleteProbation(Long id);
}