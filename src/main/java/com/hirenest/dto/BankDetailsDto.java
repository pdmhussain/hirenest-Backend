package com.hirenest.dto;


import com.hirenest.enums.BankStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankDetailsDto {

    private Long employeeId;
    private String bankName;

    private String accountNumber;

    private String ifscCode;

    private String accountHolderName;

    private BankStatus status;
}
