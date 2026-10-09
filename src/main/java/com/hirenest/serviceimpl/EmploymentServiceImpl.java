package com.hirenest.serviceimpl;

import com.hirenest.dto.EmploymentRequest;
import com.hirenest.dto.EmploymentResponse;
import com.hirenest.entity.EmploymentDetails;
import com.hirenest.enums.Domain;
import com.hirenest.enums.EmploymentStatus;
import com.hirenest.enums.EmploymentType;
import com.hirenest.exception.DuplicateResourceException;
import com.hirenest.exception.ResourceNotFoundException;
import com.hirenest.repository.EmploymentRepository;
import com.hirenest.service.EmploymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmploymentServiceImpl implements EmploymentService {

    private final EmploymentRepository repository;

    public EmploymentServiceImpl(EmploymentRepository repository) {
        this.repository = repository;
    }

    @Override
    public EmploymentResponse create(EmploymentRequest request) {
        if (repository.existsByEmployeeId(request.employeeId())) {
            throw new DuplicateResourceException(
                    "Employment details already exist for employeeId: " + request.employeeId());
        }
        EmploymentDetails entity = new EmploymentDetails();
        mapRequestToEntity(request, entity);
        return toResponse(repository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public EmploymentResponse getById(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public EmploymentResponse getByEmployeeId(Long employeeId) {
        EmploymentDetails entity = repository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employment details not found for employeeId: " + employeeId));
        return toResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmploymentResponse> getAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmploymentResponse> getByDomain(Domain domain) {
        return repository.findByDomain(domain).stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmploymentResponse> getByType(EmploymentType type) {
        return repository.findByEmploymentType(type).stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmploymentResponse> getByStatus(EmploymentStatus status) {
        return repository.findByStatus(status).stream().map(this::toResponse).toList();
    }

    @Override
    public EmploymentResponse update(Long id, EmploymentRequest request) {
        EmploymentDetails entity = findOrThrow(id);
        if (!entity.getEmployeeId().equals(request.employeeId())
                && repository.existsByEmployeeId(request.employeeId())) {
            throw new DuplicateResourceException(
                    "Employment details already exist for employeeId: " + request.employeeId());
        }
        mapRequestToEntity(request, entity);
        return toResponse(repository.save(entity));
    }

    @Override
    public EmploymentResponse updateStatus(Long id, EmploymentStatus status) {
        EmploymentDetails entity = findOrThrow(id);
        entity.setStatus(status);
        return toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        repository.delete(findOrThrow(id));
    }

    // ---------------- helpers ----------------

    private EmploymentDetails findOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employment details not found with id: " + id));
    }

    private void mapRequestToEntity(EmploymentRequest r, EmploymentDetails e) {
        e.setEmployeeId(r.employeeId());
        e.setDomain(r.domain());
        e.setEmploymentType(r.employmentType());
        e.setJoiningDate(r.joiningDate());
        e.setProbationPeriod(r.probationPeriod());

        // Probation dates: use given values, otherwise calculate from joining date
        if (r.probationPeriod() != null && r.probationPeriod() > 0) {
            e.setProbationStartDate(r.probationStartDate() != null ? r.probationStartDate() : r.joiningDate());
            e.setProbationEndDate(r.probationEndDate() != null
                    ? r.probationEndDate()
                    : e.getProbationStartDate().plusMonths(r.probationPeriod()));
        } else {
            e.setProbationStartDate(r.probationStartDate());
            e.setProbationEndDate(r.probationEndDate());
        }

        if (e.getProbationStartDate() != null && e.getProbationEndDate() != null
                && e.getProbationEndDate().isBefore(e.getProbationStartDate())) {
            throw new IllegalArgumentException("probationEndDate cannot be before probationStartDate");
        }

        if (r.status() != null) {
            e.setStatus(r.status());
        } else if (e.getStatus() == null) {
            e.setStatus(e.getProbationEndDate() != null
                    ? EmploymentStatus.ON_PROBATION
                    : EmploymentStatus.ACTIVE);
        }
    }

    private EmploymentResponse toResponse(EmploymentDetails e) {
        return new EmploymentResponse(
                e.getId(),
                e.getEmployeeId(),
                e.getDomain(),
                e.getEmploymentType(),
                e.getJoiningDate(),
                e.getStatus(),
                e.getProbationPeriod(),
                e.getProbationStartDate(),
                e.getProbationEndDate());
    }
}
