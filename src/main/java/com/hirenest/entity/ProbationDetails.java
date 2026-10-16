package com.hirenest.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "probation_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProbationDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "employee_id", nullable = false, unique = true)
    private Employee employee;

    @Column(nullable = false)
    private Integer probationPeriod; // In months

    @Column(nullable = false)
    private LocalDate probationStartDate;

    @Column(nullable = false)
    private LocalDate probationEndDate;
}