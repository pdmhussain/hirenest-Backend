package com.hirenest.controller;

import com.hirenest.dto.CompensationReportResponse;
import com.hirenest.dto.EmployeeReportResponse;
import com.hirenest.dto.ProbationReportResponse;
import com.hirenest.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeReportResponse>> getEmployeeReports() {
        return ResponseEntity.ok(
                reportService.getEmployeeReports()
        );
    }

    @GetMapping("/probation")
    public ResponseEntity<List<ProbationReportResponse>> getProbationReports(
            @RequestParam(required = false) String employee,
            @RequestParam(required = false) String domain,
            @RequestParam(required = false) String status,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate
    ) {
        return ResponseEntity.ok(
                reportService.getProbationReports(
                        employee,
                        domain,
                        status,
                        startDate,
                        endDate
                )
        );
    }

    @GetMapping("/compensation")
    public ResponseEntity<List<CompensationReportResponse>> getCompensationReports() {
        return ResponseEntity.ok(
                reportService.getCompensationReports()
        );
    }

    @GetMapping("/search")
    public ResponseEntity<List<EmployeeReportResponse>> searchEmployees(
            @RequestParam(required = false) String employeeId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String domain,
            @RequestParam(required = false) String employmentType,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate joiningDate,
            @RequestParam(required = false) String status
    ) {
        return ResponseEntity.ok(
                reportService.searchEmployees(
                        employeeId,
                        name,
                        email,
                        phone,
                        domain,
                        employmentType,
                        joiningDate,
                        status
                )
        );
    }
}