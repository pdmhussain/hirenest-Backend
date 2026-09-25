package com.hirenest.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employee_documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private String documentType;

    @Column(nullable = false)
    private String documentName;

    private String filePath;

    private LocalDateTime uploadedDate;

    private String status;

    @PrePersist
    protected void onCreate() {
        uploadedDate = LocalDateTime.now();
    }
}
