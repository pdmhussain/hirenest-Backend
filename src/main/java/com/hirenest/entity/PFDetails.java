package com.hirenest.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pf_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PFDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "employee_id", nullable = false, unique = true)
    private Employee employee;

    private String pfAccountNumber;

    private String uan;

    private String pfStatus;

    private String uanStatus;
}
