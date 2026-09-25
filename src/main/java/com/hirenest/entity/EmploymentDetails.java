package com.hirenest.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employment_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmploymentDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "employee_id", nullable = false, unique = true)
    private Employee employee;

    @Column(nullable = false)
    private String employmentType;

    @ManyToOne
    @JoinColumn(name = "domain_id")
    private Domain domain;

    @Column(nullable = false)
    private LocalDate joiningDate;

    @Column(nullable = false)
    private String status;
}
