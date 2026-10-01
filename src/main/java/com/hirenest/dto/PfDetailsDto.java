package com.hirenest.dto;


import com.hirenest.entity.Employee;


import com.hirenest.enums.PFStatus;
import com.hirenest.enums.UANStatus;
import lombok.Data;

@Data
public class PfDetailsDto {
    private Long employeeId;

    private String pfAccountNumber;

    private String uan;

    private PFStatus pfStatus;

    private UANStatus uanStatus;
}
