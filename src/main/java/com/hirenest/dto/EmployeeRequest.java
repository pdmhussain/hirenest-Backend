package com.hirenest.dto;

import java.time.LocalDate;

import lombok.Data;


@Data
public class EmployeeRequest {

    private String employeeId;
    private String fullName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private String residentialAddress;
    private String permanentAddress;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String domainName;
    private String employmentType;
    private LocalDate joiningDate;
    private String status;

    public EmployeeRequest() {
    }

}
