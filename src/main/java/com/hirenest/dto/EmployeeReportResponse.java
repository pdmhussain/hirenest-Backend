package com.hirenest.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeReportResponse {

    private String employeeId;
    private String fullName;
    private String email;
    private String phone;
    private String domain;
    private String employmentType;
    private LocalDate joiningDate;
    private String status;
}