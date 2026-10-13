package com.hirenest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    private long totalEmployees;
    private long totalInterns;
    private long totalFullTimeEmployees;
    private long totalDomains;

    private long probationInProgress;
    private long probationCompleted;
    private long probationDueSoon;

    private List<EmployeeDomainCount> employeesByDomain;
    private List<RecentEmployeeResponse> recentlyJoinedEmployees;
    private List<RecentRecordResponse> recentRecords;
}