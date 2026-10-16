package com.hirenest.service;

import java.util.List;

import com.hirenest.dto.DomainRequest;
import com.hirenest.entity.Domain;

public interface DomainService {

    Domain createDomain(DomainRequest request);

    List<Domain> getAllDomains();

    Domain getDomainById(Long id);

    Domain updateDomain(Long id, DomainRequest request);

    void deleteDomain(Long id);
}