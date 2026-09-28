package com.hirenest.entity;

import com.hirenest.enums.EmploymentType;
import com.hirenest.enums.OfferStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "offer_letters")
@Getter
@Setter
public class OfferLetter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "offer_letter_number", nullable = false, unique = true)
    private String offerLetterNumber;

    @Column(name = "offer_date", nullable = false)
    private LocalDate offerDate;

    @Column(name = "offer_letter_file")
    private String offerLetterFile;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_type", nullable = false)
    private EmploymentType employmentType;

    @Column(name = "offered_salary", precision = 12, scale = 2)
    private BigDecimal offeredSalary;

    @Column(name = "offered_stipend", precision = 12, scale = 2)
    private BigDecimal offeredStipend;

    @Column(name = "joining_date", nullable = false)
    private LocalDate joiningDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "offer_status", nullable = false)
    private OfferStatus offerStatus;
}