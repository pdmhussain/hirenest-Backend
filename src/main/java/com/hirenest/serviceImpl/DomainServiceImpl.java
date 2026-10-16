package com.hirenest.serviceImpl;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.hirenest.dto.DomainRequest;
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

    private static final Set<String> VALID_STATUSES =
            Set.of("ACTIVE", "INACTIVE");

    @Override
    public Domain createDomain(DomainRequest request) {

        validateRequest(request);

        String name = request.getName().trim();

        if (domainRepository.existsByName(name)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Domain already exists: " + name);
        }

        Domain domain = new Domain();
        domain.setName(name);
        domain.setDescription(request.getDescription());
        domain.setStatus(normalizeStatus(request.getStatus()));

        return domainRepository.save(domain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Domain> getAllDomains() {
        return domainRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Domain getDomainById(Long id) {
        return findDomain(id);
    }

    @Override
    public Domain updateDomain(Long id, DomainRequest request) {

        validateRequest(request);

        Domain domain = findDomain(id);
        String name = request.getName().trim();

        domainRepository.findByName(name).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Another domain already uses this name.");
            }
        });

        domain.setName(name);
        domain.setDescription(request.getDescription());
        domain.setStatus(normalizeStatus(request.getStatus()));

        return domainRepository.save(domain);
    }

    @Override
    public void deleteDomain(Long id) {

        Domain domain = findDomain(id);

        // Preserve employees and prevent foreign-key violations.
        if (employeeRepository.existsByDomain_Id(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cannot delete this domain because employees "
                            + "are assigned to it. Reassign the employees "
                            + "or mark the domain INACTIVE.");
        }

        domainRepository.delete(domain);
    }

    private Domain findDomain(Long id) {
        return domainRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Domain not found with ID: " + id));
    }

    private void validateRequest(DomainRequest request) {

        if (request == null
                || request.getName() == null
                || request.getName().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Domain name is required.");
        }

        if (request.getStatus() != null
                && !request.getStatus().isBlank()
                && !VALID_STATUSES.contains(
                request.getStatus().trim().toUpperCase())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Status must be ACTIVE or INACTIVE.");
        }
    }

    private String normalizeStatus(String status) {
        if (status == null || status.isBlank()) {
            return "ACTIVE";
        }

        return status.trim().toUpperCase();
    }
}