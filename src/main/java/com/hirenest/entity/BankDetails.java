package com.hirenest.entity;

import com.hirenest.enums.BankStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bank_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "employee_id", nullable = false, unique = true)
    private Employee employee;

    private String bankName;

    private String accountNumber;

    private String ifscCode;

    private String accountHolderName;

    @Enumerated(EnumType.STRING)
    private BankStatus status;
}
