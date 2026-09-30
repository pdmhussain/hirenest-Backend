package com.hirenest.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hirenest.entity.Employee;


public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmployeeId(String employeeId);

    boolean existsByEmployeeId(String employeeId);

    boolean existsByEmail(String email);
}
