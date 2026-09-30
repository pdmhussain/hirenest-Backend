package com.hirenest.service;


import java.util.List;

import com.hirenest.dto.EmployeeRequest;
import com.hirenest.dto.EmployeeResponse;


public interface EmployeeService {

    EmployeeResponse createEmployee(EmployeeRequest request);

    List<EmployeeResponse> getAllEmployees();

    EmployeeResponse getEmployeeById(Long id);

    EmployeeResponse updateEmployee(Long id, EmployeeRequest request);

    void deleteEmployee(Long id);
}
