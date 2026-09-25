package com.hirenest.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "salary_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SalaryDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private BigDecimal salary;

    @Column(nullable = false)
    private String frequency;

    @Column(nullable = false)
    private LocalDate effectiveDate;

    @Column(nullable = false)
    private String currency;
}
