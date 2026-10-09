package com.hirenest.service;

import com.hirenest.dto.DomainRequest;
import com.hirenest.entity.Domain;

import java.util.List;

public interface DomainService {

    Domain createDomain(DomainRequest request);

    Domain getDomainById(Long id);

    List<Domain> getAllDomains();

    Domain updateDomain(Long id, DomainRequest request);

    void deleteDomain(Long id);
}