package com.hirenest.service;

import java.util.List;

import com.hirenest.dto.DomainRequest;
import com.hirenest.dto.DomainResponse;

public interface DomainService {

    DomainResponse createDomain(DomainRequest request);

    List<DomainResponse> getAllDomains();

    DomainResponse getDomainById(Long id);

    DomainResponse updateDomain(Long id, DomainRequest request);

    void deleteDomain(Long id);
}
