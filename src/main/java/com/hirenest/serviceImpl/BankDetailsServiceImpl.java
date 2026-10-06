package com.hirenest.serviceImpl;

import com.hirenest.dto.BankDetailsDto;
import com.hirenest.entity.BankDetails;
import com.hirenest.entity.Employee;
import com.hirenest.repository.BankDetailsRepository;
import com.hirenest.repository.EmployeeRepository;
import com.hirenest.service.BankDetailsService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BankDetailsServiceImpl implements BankDetailsService {

    private final BankDetailsRepository bankDetailsRepository;
    private final EmployeeRepository employeeRepository;


    @Override
    public BankDetails createBankDetails(
            BankDetailsDto request,
            String email) {

        Employee employee = employeeRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employee not found with email: " + email));

        if (bankDetailsRepository.findByEmployee_Id(employee.getId()).isPresent()) {
            throw new RuntimeException(
                    "Bank details already exist for this employee");
        }

        BankDetails bankDetails = new BankDetails();

        bankDetails.setEmployee(employee);
        bankDetails.setBankName(request.getBankName());
        bankDetails.setAccountNumber(request.getAccountNumber());
        bankDetails.setIfscCode(request.getIfscCode());
        bankDetails.setAccountHolderName(request.getAccountHolderName());
        bankDetails.setStatus(request.getStatus());

        return bankDetailsRepository.save(bankDetails);
    }

    @Override
    public List<BankDetails> getAllBankDetails() {

        return bankDetailsRepository.findAll();
    }


    @Override
    public BankDetails getByEmployeeId(Long employeeId) {

        return bankDetailsRepository
                .findByEmployee_Id(employeeId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Bank details not found for employee ID: "
                                        + employeeId
                        )
                );
    }
}