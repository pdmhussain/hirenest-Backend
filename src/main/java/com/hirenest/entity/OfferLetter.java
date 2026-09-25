package com.hirenest.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "offer_letters")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OfferLetter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false, unique = true)
    private String offerNumber;

    @Column(nullable = false)
    private LocalDate offerDate;

    private String filePath;

    private String employmentType;

    private BigDecimal salaryOrStipend;

    private LocalDate joiningDate;

    private String status;
}
