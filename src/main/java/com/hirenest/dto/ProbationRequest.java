package com.hirenest.dto;

import java.time.LocalDate;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProbationRequest {

    private Long employeeId;

    private Integer probationPeriod;

    private LocalDate probationStartDate;

    private LocalDate probationEndDate;

    private String status;

    private String extensionReason;
}