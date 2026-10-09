package com.hirenest.serviceImpl;

import com.hirenest.dto.DomainRequest;
import com.hirenest.entity.Domain;
import com.hirenest.repository.DomainRepository;
import com.hirenest.service.DomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DomainServiceImpl implements DomainService {

    private final DomainRepository domainRepository;

    @Override
    public Domain createDomain(DomainRequest request) {

        Domain domain = new Domain();
        domain.setName(request.getName());
        domain.setDescription(request.getDescription());
        domain.setStatus(request.getStatus());

        return domainRepository.save(domain);
    }

    @Override
    public Domain getDomainById(Long id) {
        return domainRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Domain not found with id: " + id));
    }

    @Override
    public List<Domain> getAllDomains() {
        return domainRepository.findAll();
    }

    @Override
    public Domain updateDomain(Long id, DomainRequest request) {

        Domain domain = getDomainById(id);

        domain.setName(request.getName());
        domain.setDescription(request.getDescription());
        domain.setStatus(request.getStatus());

        return domainRepository.save(domain);
    }

    @Override
    public void deleteDomain(Long id) {

        Domain domain = getDomainById(id);

        domainRepository.delete(domain);
    }
}