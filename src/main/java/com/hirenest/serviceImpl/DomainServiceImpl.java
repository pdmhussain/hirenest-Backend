package com.hirenest.serviceImpl;
import java.util.List;
import java.util.Locale;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.hirenest.dto.DomainRequest;
import com.hirenest.dto.DomainResponse;
import com.hirenest.entity.Domain;
import com.hirenest.repository.DomainRepository;
import com.hirenest.repository.EmployeeRepository;
import com.hirenest.service.DomainService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DomainServiceImpl implements DomainService {

    private final DomainRepository domainRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public DomainResponse createDomain(DomainRequest request) {
        validateName(request.getName());

        String name = request.getName().trim();

        if (domainRepository.existsByNameIgnoreCase(name)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "A domain with this name already exists");
        }

        Domain domain = new Domain();
        domain.setName(name);
        domain.setDescription(request.getDescription());
        domain.setStatus(normalizeStatus(request.getStatus()));

        return toResponse(domainRepository.save(domain));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DomainResponse> getAllDomains() {
        return domainRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DomainResponse getDomainById(Long id) {
        return toResponse(findDomain(id));
    }

    @Override
    public DomainResponse updateDomain(Long id, DomainRequest request) {
        Domain domain = findDomain(id);

        validateName(request.getName());
        String name = request.getName().trim();

        domainRepository.findByNameIgnoreCase(name)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new ResponseStatusException(
                            HttpStatus.CONFLICT,
                            "A domain with this name already exists");
                });

        domain.setName(name);
        domain.setDescription(request.getDescription());

        if (request.getStatus() != null
                && !request.getStatus().isBlank()) {
            domain.setStatus(normalizeStatus(request.getStatus()));
        }

        return toResponse(domainRepository.save(domain));
    }

    @Override
    public void deleteDomain(Long id) {
        Domain domain = findDomain(id);

        if (employeeRepository.existsByDomain_Id(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cannot delete a domain assigned to employees");
        }

        domainRepository.delete(domain);
    }

    private Domain findDomain(Long id) {
        return domainRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Domain not found with ID: " + id));
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Domain name is required");
        }
    }

    private String normalizeStatus(String status) {
        if (status == null || status.isBlank()) {
            return "ACTIVE";
        }

        String normalized = status.trim().toUpperCase(Locale.ROOT);

        if (!normalized.equals("ACTIVE")
                && !normalized.equals("INACTIVE")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Status must be ACTIVE or INACTIVE");
        }

        return normalized;
    }

    private DomainResponse toResponse(Domain domain) {
        return new DomainResponse(
                domain.getId(),
                domain.getName(),
                domain.getDescription(),
                domain.getStatus());
    }
}
