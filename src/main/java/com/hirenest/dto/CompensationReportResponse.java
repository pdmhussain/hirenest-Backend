package com.hirenest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompensationReportResponse {

    private String employeeId;
    private String employeeName;
    private String employmentType;
    private BigDecimal salary;
    private String frequency;
    private String currency;
    private LocalDate effectiveDate;
}