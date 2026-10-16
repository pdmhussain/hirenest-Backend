package com.hirenest.serviceImpl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hirenest.dto.ProbationRequest;
import com.hirenest.dto.ProbationResponse;
import com.hirenest.entity.Employee;
import com.hirenest.entity.ProbationDetails;
import com.hirenest.exception.ResourceNotFoundException;
import com.hirenest.repository.EmployeeRepository;
import com.hirenest.repository.ProbationRepository;
import com.hirenest.service.ProbationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProbationServiceImpl implements ProbationService {

    private final ProbationRepository probationRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public ProbationResponse createProbation(ProbationRequest request) {

        validateRequest(request);

        if (probationRepository.existsByEmployeeEmployeeId(
                request.getEmployeeId())) {
            throw new IllegalArgumentException(
                    "Probation details already exist for employee: "
                            + request.getEmployeeId());
        }

        Employee employee = findEmployee(request.getEmployeeId());

        LocalDate joiningDate = employee.getJoiningDate();

        if (joiningDate == null) {
            throw new IllegalArgumentException(
                    "Joining date is missing for employee: "
                            + request.getEmployeeId());
        }

        LocalDate startDate = request.getProbationStartDate() != null
                ? request.getProbationStartDate()
                : joiningDate;

        validateStartDate(startDate, joiningDate);

        LocalDate endDate = calculateEndDate(
                startDate, request.getProbationPeriod());

        ProbationDetails probation = new ProbationDetails();
        probation.setEmployee(employee);
        probation.setProbationPeriod(request.getProbationPeriod());
        probation.setProbationStartDate(startDate);
        probation.setProbationEndDate(endDate);

        ProbationDetails saved = probationRepository.save(probation);

        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProbationResponse> getAllProbations() {

        return probationRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProbationResponse getProbationByEmployeeId(String employeeId) {

        ProbationDetails probation = probationRepository
                .findByEmployeeEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Probation details not found for employee: "
                                + employeeId));

        return toResponse(probation);
    }

    @Override
    public ProbationResponse updateProbation(
            String employeeId,
            ProbationRequest request) {

        validateRequest(request);

        ProbationDetails probation = probationRepository
                .findByEmployeeEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Probation details not found for employee: "
                                + employeeId));

        if (request.getEmployeeId() != null
                && !request.getEmployeeId().equals(employeeId)) {
            throw new IllegalArgumentException(
                    "Employee ID cannot be changed during probation update.");
        }

        Employee employee = probation.getEmployee();

        if (employee.getJoiningDate() == null) {
            throw new IllegalArgumentException(
                    "Joining date is missing for employee: " + employeeId);
        }

        LocalDate startDate = request.getProbationStartDate() != null
                ? request.getProbationStartDate()
                : probation.getProbationStartDate();

        Integer period = request.getProbationPeriod() != null
                ? request.getProbationPeriod()
                : probation.getProbationPeriod();

        validatePeriod(period);
        validateStartDate(startDate, employee.getJoiningDate());

        LocalDate endDate = calculateEndDate(startDate, period);

        probation.setProbationStartDate(startDate);
        probation.setProbationPeriod(period);
        probation.setProbationEndDate(endDate);

        return toResponse(probationRepository.save(probation));
    }

    @Override
    public void deleteProbation(String employeeId) {

        ProbationDetails probation = probationRepository
                .findByEmployeeEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Probation details not found for employee: "
                                + employeeId));

        probationRepository.delete(probation);
    }

    private Employee findEmployee(String employeeId) {

        return employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found: " + employeeId));
    }

    private LocalDate calculateEndDate(
            LocalDate startDate, Integer probationPeriod) {

        return startDate.plusMonths(probationPeriod).minusDays(1);
    }

    private String calculateStatus(
            LocalDate startDate,
            LocalDate endDate,
            LocalDate currentDate) {

        if (currentDate.isBefore(startDate)) {
            return "NOT_STARTED";
        }

        if (currentDate.isAfter(endDate)) {
            return "COMPLETED";
        }

        return "IN_PROGRESS";
    }

    private ProbationResponse toResponse(ProbationDetails probation) {

        LocalDate currentDate = LocalDate.now();

        Employee employee = probation.getEmployee();

        String status = calculateStatus(
                probation.getProbationStartDate(),
                probation.getProbationEndDate(),
                currentDate);

        return new ProbationResponse(
                employee.getEmployeeId(),
                employee.getJoiningDate(),
                probation.getProbationPeriod(),
                probation.getProbationStartDate(),
                probation.getProbationEndDate(),
                currentDate,
                status);
    }

    private void validateRequest(ProbationRequest request) {

        if (request == null || request.getEmployeeId() == null
                || request.getEmployeeId().isBlank()) {
            throw new IllegalArgumentException("Employee ID is required.");
        }

        validatePeriod(request.getProbationPeriod());
    }

    private void validatePeriod(Integer period) {

        if (period == null || period <= 0) {
            throw new IllegalArgumentException(
                    "Probation period must be greater than zero months.");
        }
    }

    private void validateStartDate(
            LocalDate startDate, LocalDate joiningDate) {

        if (startDate == null) {
            throw new IllegalArgumentException(
                    "Probation start date is required.");
        }

        if (startDate.isBefore(joiningDate)) {
            throw new IllegalArgumentException(
                    "Probation start date cannot be before joining date.");
        }
    }
}