
package com.hirenest.service;

import java.util.List;

import com.hirenest.dto.ProbationRequest;
import com.hirenest.dto.ProbationResponse;
import com.hirenest.dto.ProbationDomainSummaryResponse;

public interface ProbationService {

    ProbationResponse createProbation(ProbationRequest request);

    List<ProbationResponse> getAllProbations();

    ProbationResponse getProbationByEmployeeId(String employeeId);

    ProbationResponse updateProbation(
            String employeeId,
            ProbationRequest request);

    void deleteProbation(String employeeId);

    List<ProbationResponse> getProbationsByStatus(String status);

    ProbationDomainSummaryResponse getProbationDomainSummary(
            Long domainId);
}
