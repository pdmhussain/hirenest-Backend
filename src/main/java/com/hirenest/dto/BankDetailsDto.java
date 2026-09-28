package com.hirenest.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankDetailsDto {

    private Long EmployeeId;
    private String bankName;

    private String accountNumber;

    private String ifscCode;

    private String accountHolderName;

    private String status;
}
