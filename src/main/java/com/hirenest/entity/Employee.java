package com.hirenest.entity;



import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String employeeId;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    private LocalDate dateOfBirth;

    private String residentialAddress;

    private String permanentAddress;

    private String emergencyContactName;

    private String emergencyContactPhone;

    @Column(nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "domain_id")
    private Domain domain;
    
    
    private String employmentType;

    private LocalDate joiningDate;
    
    public void SetDomainName(String name) {
        if (this.domain == null) {
            this.domain = new Domain();
        }

        this.domain.setName(name);
    }
    public String getDomainName() {
    	
    	return domain.getName();
    }
    
}
