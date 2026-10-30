package com.hirenest.serviceImpl;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hirenest.dto.ProbationDomainSummaryResponse;
import com.hirenest.dto.ProbationRequest;
import com.hirenest.dto.ProbationResponse;
import com.hirenest.entity.Domain;
import com.hirenest.entity.Employee;
import com.hirenest.entity.ProbationDetails;
import com.hirenest.exception.ResourceNotFoundException;
import com.hirenest.repository.DomainRepository;
import com.hirenest.repository.EmployeeRepository;
import com.hirenest.repository.ProbationRepository;
import com.hirenest.service.ProbationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProbationServiceImpl implements ProbationService {

    private static final int DUE_SOON_DAYS = 15;

    private static final Set<String> ALLOWED_STATUSES = Set.of(
            "NOT_STARTED",
            "IN_PROGRESS",
            "DUE_SOON",
            "COMPLETED",
            "EXTENDED"
    );

    private final ProbationRepository probationRepository;
    private final EmployeeRepository employeeRepository;
    private final DomainRepository domainRepository;

    @Override
    public ProbationResponse createProbation(ProbationRequest request) {

        validateRequest(request);

        String employeeId = request.getEmployeeId().trim();

        if (probationRepository.existsByEmployeeEmployeeId(employeeId)) {
            throw new IllegalArgumentException(
                    "Probation already exists for employee: " + employeeId);
        }

        Employee employee = findEmployee(employeeId);
        LocalDate joiningDate = employee.getJoiningDate();

        if (joiningDate == null) {
            throw new IllegalArgumentException(
                    "Joining date is missing for employee: " + employeeId);
        }

        // Automatically use the employee's joining date.
        LocalDate startDate = joiningDate;

        // Automatically calculate the end date from the period.
        LocalDate endDate = calculateEndDate(
                startDate, request.getProbationPeriod());

        ProbationDetails probation = new ProbationDetails();
        probation.setEmployee(employee);
        probation.setProbationPeriod(request.getProbationPeriod());
        probation.setProbationStartDate(startDate);
        probation.setProbationEndDate(endDate);

        String extensionReason = request.getExtensionReason();

        probation.setExtensionReason(
                extensionReason == null || extensionReason.isBlank()
                        ? null
                        : extensionReason.trim());

        // Never set EXTENDED automatically during creation.
        probation.setProbationStatus(
                calculateStatus(startDate, endDate, LocalDate.now()));

        return toResponse(probationRepository.save(probation));
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

        return toResponse(findProbation(employeeId));
    }

    @Override
    public ProbationResponse updateProbation(
            String employeeId,
            ProbationRequest request) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Probation request is required.");
        }

        if (employeeId == null || employeeId.isBlank()) {
            throw new IllegalArgumentException("Employee ID is required.");
        }

        employeeId = employeeId.trim();

        if (request.getEmployeeId() != null
                && !request.getEmployeeId().isBlank()
                && !request.getEmployeeId().trim().equals(employeeId)) {
            throw new IllegalArgumentException(
                    "Employee ID cannot be changed during an update.");
        }

        ProbationDetails probation = findProbation(employeeId);
        Employee employee = probation.getEmployee();

        if (employee.getJoiningDate() == null) {
            throw new IllegalArgumentException(
                    "Joining date is missing for employee: " + employeeId);
        }

        Integer period = request.getProbationPeriod() != null
                ? request.getProbationPeriod()
                : probation.getProbationPeriod();

        validatePeriod(period);

        // Keep the joining date as the probation start date.
        LocalDate startDate = employee.getJoiningDate();

        // Recalculate the end date if the probation period changes.
        LocalDate endDate = request.getProbationPeriod() != null
                ? calculateEndDate(startDate, period)
                : probation.getProbationEndDate();

        validateStartDate(startDate, employee.getJoiningDate());
        validateEndDate(startDate, endDate);

        probation.setProbationPeriod(period);
        probation.setProbationStartDate(startDate);
        probation.setProbationEndDate(endDate);

        if (request.getExtensionReason() != null) {
            String reason = request.getExtensionReason().trim();

            probation.setExtensionReason(
                    reason.isEmpty() ? null : reason);
        }

        // Status is calculated from dates, not from the extension reason.
        probation.setProbationStatus(
                calculateStatus(startDate, endDate, LocalDate.now()));

        return toResponse(probationRepository.save(probation));
    }

    @Override
    public void deleteProbation(String employeeId) {

        probationRepository.delete(findProbation(employeeId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProbationResponse> getProbationsByStatus(String status) {

        String requestedStatus = normalizeStatus(status);

        if (requestedStatus == null) {
            throw new IllegalArgumentException(
                    "A valid probation status is required.");
        }

        return probationRepository.findAll()
                .stream()
                .map(this::toResponse)
                .filter(response ->
                        requestedStatus.equals(response.getProbationStatus()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProbationDomainSummaryResponse getProbationDomainSummary(
            Long domainId) {

        if (domainId == null) {
            throw new IllegalArgumentException("Domain ID is required.");
        }

        Domain domain = domainRepository.findById(domainId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Domain not found: " + domainId));

        long totalEmployees =
                probationRepository.countActiveEmployeesByDomain(domainId);

        long completed =
                probationRepository.countCompletedProbationsByDomain(domainId);

        long inProgress =
                probationRepository.countInProgressProbationsByDomain(domainId);

        return new ProbationDomainSummaryResponse(
                domain.getId(),
                domain.getName(),
                totalEmployees,
                completed,
                inProgress
        );
    }

    private Employee findEmployee(String employeeId) {

        return employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found: " + employeeId));
    }

    private ProbationDetails findProbation(String employeeId) {

        if (employeeId == null || employeeId.isBlank()) {
            throw new IllegalArgumentException("Employee ID is required.");
        }

        return probationRepository
                .findByEmployeeEmployeeId(employeeId.trim())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Probation not found for employee: " + employeeId));
    }

    private LocalDate calculateEndDate(
            LocalDate startDate, Integer period) {

        return startDate.plusMonths(period).minusDays(1);
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

        long daysRemaining = ChronoUnit.DAYS.between(currentDate, endDate);

        if (daysRemaining <= DUE_SOON_DAYS) {
            return "DUE_SOON";
        }

        return "IN_PROGRESS";
    }

    private String normalizeStatus(String status) {

        if (status == null || status.isBlank()) {
            return null;
        }

        String normalized = status.trim()
                .toUpperCase()
                .replace(' ', '_');

        if (!ALLOWED_STATUSES.contains(normalized)) {
            throw new IllegalArgumentException(
                    "Invalid probation status. Allowed statuses: "
                            + ALLOWED_STATUSES);
        }

        return normalized;
    }

    private ProbationResponse toResponse(ProbationDetails probation) {

        LocalDate currentDate = LocalDate.now();

        // Always calculate status from dates, including for old records
        // that previously stored EXTENDED.
        String status = calculateStatus(
                probation.getProbationStartDate(),
                probation.getProbationEndDate(),
                currentDate);

        Employee employee = probation.getEmployee();

        return new ProbationResponse(
                employee.getEmployeeId(),
                employee.getJoiningDate(),
                probation.getProbationPeriod(),
                probation.getProbationStartDate(),
                probation.getProbationEndDate(),
                currentDate,
                status,
                probation.getExtensionReason()
        );
    }

    private void validateRequest(ProbationRequest request) {

        if (request == null
                || request.getEmployeeId() == null
                || request.getEmployeeId().isBlank()) {
            throw new IllegalArgumentException("Employee ID is required.");
        }

        validatePeriod(request.getProbationPeriod());
    }

    private void validatePeriod(Integer period) {

        if (period == null || period <= 0) {
            throw new IllegalArgumentException(
                    "Probation period must be greater than zero.");
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

    private void validateEndDate(
            LocalDate startDate, LocalDate endDate) {

        if (endDate == null || endDate.isBefore(startDate)) {
            throw new IllegalArgumentException(
                    "Probation end date cannot be before start date.");
        }
    }
}
