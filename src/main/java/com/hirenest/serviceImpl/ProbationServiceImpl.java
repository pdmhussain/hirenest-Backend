package com.hirenest.service.impl;

import com.hirenest.dto.ProbationRequest;
import com.hirenest.entity.Employee;
import com.hirenest.entity.ProbationDetails;
import com.hirenest.repository.EmployeeRepository;
import com.hirenest.repository.ProbationRepository;
import com.hirenest.service.ProbationService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProbationServiceImpl implements ProbationService {

    private final ProbationRepository probationRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public ProbationDetails createProbation(ProbationRequest request) {

        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() ->
                        new RuntimeException("Employee not found with id: "
                                + request.getEmployeeId()));

        ProbationDetails probation = new ProbationDetails();

        probation.setEmployee(employee);
        probation.setProbationPeriod(request.getProbationPeriod());
        probation.setProbationStartDate(request.getProbationStartDate());
        probation.setProbationEndDate(request.getProbationEndDate());
        probation.setStatus(request.getStatus());
        probation.setExtensionReason(request.getExtensionReason());

        return probationRepository.save(probation);
    }

    @Override
    public ProbationDetails getProbationById(Long id) {

        return probationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Probation not found with id: " + id));
    }

    @Override
    public List<ProbationDetails> getAllProbations() {

        return probationRepository.findAll();
    }

    @Override
    public ProbationDetails updateProbation(
            Long id,
            ProbationRequest request) {

        ProbationDetails probation = getProbationById(id);

        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() ->
                        new RuntimeException("Employee not found with id: "
                                + request.getEmployeeId()));

        probation.setEmployee(employee);
        probation.setProbationPeriod(request.getProbationPeriod());
        probation.setProbationStartDate(request.getProbationStartDate());
        probation.setProbationEndDate(request.getProbationEndDate());
        probation.setStatus(request.getStatus());
        probation.setExtensionReason(request.getExtensionReason());

        return probationRepository.save(probation);
    }

    @Override
    public void deleteProbation(Long id) {

        ProbationDetails probation = getProbationById(id);

        probationRepository.delete(probation);
    }
}