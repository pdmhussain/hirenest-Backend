package com.hirenest.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class OfferLetterResponse {

    private Long id;

    private Long employeeId;

    private String offerLetterNumber;

    private LocalDate offerDate;

    private String offerLetterFile;

    private String employmentType;

    private BigDecimal offeredSalary;

    private BigDecimal offeredStipend;

    private LocalDate joiningDate;

    private String offerStatus;
}