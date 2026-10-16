package com.hirenest.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ProbationRequest {

    private String employeeId;

    private Integer probationPeriod;

    private LocalDate probationStartDate;

    private LocalDate probationEndDate;
}