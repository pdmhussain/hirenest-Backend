package com.hirenest.service;

import com.hirenest.dto.DashboardResponse;
import com.hirenest.repository.DomainRepository;
import com.hirenest.repository.EmployeeRepository;
import com.hirenest.repository.EmploymentDetailsRepository;
import com.hirenest.repository.ProbationDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final EmployeeRepository employeeRepository;
    private final DomainRepository domainRepository;
    private final EmploymentDetailsRepository employmentDetailsRepository;
    private final ProbationDetailsRepository probationDetailsRepository;

    public DashboardResponse getDashboard() {

        LocalDate today = LocalDate.now();
        LocalDate dueSoonDate = today.plusDays(7);

        long totalEmployees =
                employeeRepository.count();

        long totalInterns =
                employmentDetailsRepository.countInterns();

        long totalFullTimeEmployees =
                employmentDetailsRepository.countFullTimeEmployees();

        long totalDomains =
                domainRepository.count();

        long probationInProgress =
                probationDetailsRepository.countProbationInProgress();

        long probationCompleted =
                probationDetailsRepository.countProbationCompleted();

        long probationDueSoon =
                probationDetailsRepository.countProbationDueSoon(
                        today,
                        dueSoonDate
                );

        var employeesByDomain =
                domainRepository.countEmployeesByDomain();

        var recentlyJoinedEmployees =
                employmentDetailsRepository.findRecentlyJoinedEmployees(
                        PageRequest.of(0, 5)
                );

        var recentRecords =
                employmentDetailsRepository.findRecentRecords(
                        PageRequest.of(0, 5)
                );

        return new DashboardResponse(
                totalEmployees,
                totalInterns,
                totalFullTimeEmployees,
                totalDomains,
                probationInProgress,
                probationCompleted,
                probationDueSoon,
                employeesByDomain,
                recentlyJoinedEmployees,
                recentRecords
        );
    }
}