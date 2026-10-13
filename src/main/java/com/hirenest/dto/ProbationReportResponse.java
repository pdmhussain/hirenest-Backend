package com.hirenest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProbationReportResponse {

    private String employeeId;
    private String employeeName;
    private String domain;
    private LocalDate joiningDate;
    private Integer probationPeriod;
    private LocalDate probationStartDate;
    private LocalDate probationEndDate;
    private String probationStatus;
}