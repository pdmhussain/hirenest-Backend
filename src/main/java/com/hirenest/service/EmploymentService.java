package com.hirenest.service;

import com.hirenest.dto.EmploymentRequest;
import com.hirenest.dto.EmploymentResponse;
import com.hirenest.enums.Domain;
import com.hirenest.enums.EmploymentStatus;
import com.hirenest.enums.EmploymentType;

import java.util.List;

public interface EmploymentService {

    EmploymentResponse create(EmploymentRequest request);

    EmploymentResponse getById(Long id);

    EmploymentResponse getByEmployeeId(Long employeeId);

    List<EmploymentResponse> getAll();

    List<EmploymentResponse> getByDomain(Domain domain);

    List<EmploymentResponse> getByType(EmploymentType type);

    List<EmploymentResponse> getByStatus(EmploymentStatus status);

    EmploymentResponse update(Long id, EmploymentRequest request);

    EmploymentResponse updateStatus(Long id, EmploymentStatus status);

    void delete(Long id);
}
