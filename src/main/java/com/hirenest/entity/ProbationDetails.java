
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "employee_id",
            referencedColumnName = "employee_id",
            nullable = false,
            unique = true
    )
    private Employee employee;

    @Column(name = "probation_period", nullable = false)
    private Integer probationPeriod;

    @Column(name = "probation_start_date", nullable = false)
    private LocalDate probationStartDate;

    @Column(name = "probation_end_date", nullable = false)
    private LocalDate probationEndDate;

    @Column(name = "probation_status", nullable = false, length = 30)
    private String probationStatus;

    @Column(name = "extension_reason", length = 1000)
    private String extensionReason;
}
