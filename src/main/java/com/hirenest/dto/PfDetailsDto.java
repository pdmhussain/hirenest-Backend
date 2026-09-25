package com.hirenest.dto;

import com.hirenest.entity.Employee;
import lombok.Data;

@Data
public class PfDetailsDto {
    private Long employeeId;

    private String pfAccountNumber;

    private String uan;

    private String pfStatus;

    private String uanStatus;
}
