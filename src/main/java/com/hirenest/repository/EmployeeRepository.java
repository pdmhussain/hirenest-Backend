package com.hirenest.service.impl;

import com.hirenest.repository.EmployeeRepository;
import com.hirenest.service.ReportService;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService {

    private final EmployeeRepository employeeRepository;

    public ReportServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
}