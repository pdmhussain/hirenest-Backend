package com.hirenest.service.impl;

import com.hirenest.dto.CompensationReportResponse;
import com.hirenest.dto.EmployeeReportResponse;
import com.hirenest.dto.ProbationReportResponse;
import com.hirenest.repository.EmployeeRepository;
import com.hirenest.repository.EmploymentDetailsRepository;
import com.hirenest.repository.ProbationDetailsRepository;
import com.hirenest.repository.SalaryDetailsRepository;
import com.hirenest.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final EmployeeRepository employeeRepository;
    private final EmploymentDetailsRepository employmentDetailsRepository;
    private final ProbationDetailsRepository probationDetailsRepository;
    private final SalaryDetailsRepository salaryDetailsRepository;

    @Override
    public List<EmployeeReportResponse> getEmployeeReports() {
        return employmentDetailsRepository.findEmployeeReports();
    }

    @Override
    public List<ProbationReportResponse> getProbationReports(
            String employee,
            String domain,
            String status,
            LocalDate startDate,
            LocalDate endDate
    ) {
        return probationDetailsRepository.findProbationReports(
                employee,
                domain,
                status,
                startDate,
                endDate
        );
    }

    @Override
    public List<CompensationReportResponse> getCompensationReports() {
        return salaryDetailsRepository.findCompensationReports();
    }

    @Override
    public List<EmployeeReportResponse> searchEmployees(
            String employeeId,
            String name,
            String email,
            String phone,
            String domain,
            String employmentType,
            LocalDate joiningDate,
            String status
    ) {
        return employeeRepository.searchEmployees(
                employeeId,
                name,
                email,
                phone,
                domain,
                employmentType,
                joiningDate,
                status
        );
    }
}