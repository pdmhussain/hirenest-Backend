package com.hirenest.service;

import com.hirenest.dto.BankDetailsDto;
import com.hirenest.entity.BankDetails;

import java.util.List;

public interface BankDetailsService {

    BankDetails createBankDetails(BankDetailsDto request, String email);

    List<BankDetails> getAllBankDetails();

    BankDetails getByEmployeeId(Long employeeId);

    BankDetails updateBankDetails(Long id, BankDetailsDto request);

    void deleteBankDetails(Long id);




}
