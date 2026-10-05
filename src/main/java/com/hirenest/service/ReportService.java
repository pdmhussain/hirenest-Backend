package com.hirenest.service;

import com.hirenest.dto.CompensationReportResponse;
import com.hirenest.dto.EmployeeReportResponse;
import com.hirenest.dto.ProbationReportResponse;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {

    List<EmployeeReportResponse> getEmployeeReports();

    List<ProbationReportResponse> getProbationReports(
            String employee,
            String domain,
            String status,
            LocalDate startDate,
            LocalDate endDate
    );

    List<CompensationReportResponse> getCompensationReports();
}