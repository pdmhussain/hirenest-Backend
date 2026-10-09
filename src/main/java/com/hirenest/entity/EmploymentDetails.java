package com.hirenest.entity;

import com.hirenest.enums.Domain;
import com.hirenest.enums.EmploymentStatus;
import com.hirenest.enums.EmploymentType;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "employment_details")
public class EmploymentDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Refers to Employee.id (Employee module is owned by another member)
    @Column(name = "employee_id", nullable = false, unique = true)
    private Long employeeId;

    // One of the fixed domains (Java, Python, React Js, ...) stored as the enum name
    @Enumerated(EnumType.STRING)
    @Column(name = "domain", nullable = false, length = 30)
    private Domain domain;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_type", nullable = false, length = 20)
    private EmploymentType employmentType;

    @Column(name = "joining_date", nullable = false)
    private LocalDate joiningDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EmploymentStatus status;

    // in months
    @Column(name = "probation_period")
    private Integer probationPeriod;

    @Column(name = "probation_start_date")
    private LocalDate probationStartDate;

    @Column(name = "probation_end_date")
    private LocalDate probationEndDate;

    public EmploymentDetails() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

    public Domain getDomain() { return domain; }
    public void setDomain(Domain domain) { this.domain = domain; }

    public EmploymentType getEmploymentType() { return employmentType; }
    public void setEmploymentType(EmploymentType employmentType) { this.employmentType = employmentType; }

    public LocalDate getJoiningDate() { return joiningDate; }
    public void setJoiningDate(LocalDate joiningDate) { this.joiningDate = joiningDate; }

    public EmploymentStatus getStatus() { return status; }
    public void setStatus(EmploymentStatus status) { this.status = status; }

    public Integer getProbationPeriod() { return probationPeriod; }
    public void setProbationPeriod(Integer probationPeriod) { this.probationPeriod = probationPeriod; }

    public LocalDate getProbationStartDate() { return probationStartDate; }
    public void setProbationStartDate(LocalDate probationStartDate) { this.probationStartDate = probationStartDate; }

    public LocalDate getProbationEndDate() { return probationEndDate; }
    public void setProbationEndDate(LocalDate probationEndDate) { this.probationEndDate = probationEndDate; }
}
