package com.hirenest.dto;


import com.hirenest.entity.Employee;


import com.hirenest.enums.PFStatus;
import com.hirenest.enums.UANStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PfDetailsDto {
    private String pfAccountNumber;

    private String uan;

    private PFStatus pfStatus;

    private UANStatus uanStatus;
}
