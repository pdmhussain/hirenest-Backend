package com.hirenest.service;

import com.hirenest.dto.BankDetailsDto;
import com.hirenest.entity.BankDetails;
import com.hirenest.entity.Employee;
import com.hirenest.repository.BankDetailsRepository;
import com.hirenest.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BankDetailsServiceImpl implements BankDetailsService {

    private final BankDetailsRepository repository;
    private final EmployeeRepository employeeRepository;

    @Override
    public BankDetails createBankDetails(BankDetailsDto request) {
        BankDetails bankDetails = new BankDetails();
        Employee employee = employeeRepository
                .findById(request.getEmployeeId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employee not found with ID: "
                                        + request.getEmployeeId()
                        )
                );
        bankDetails.setEmployee(employee);
        bankDetails.setAccountHolderName(request.getAccountHolderName());
        bankDetails.setBankName(request.getBankName());
        bankDetails.setAccountNumber(request.getAccountNumber());
        bankDetails.setIfscCode(request.getIfscCode());
        bankDetails.setStatus(request.getStatus());


        return repository.save(bankDetails);
    }

    @Override
    public List<BankDetails> getAllBankDetails() {
        List<BankDetails> list = repository.findAll();
        return list;
    }

    @Override
    public BankDetails getByEmployeeId(Long employeeId) {
        return repository.findByEmployee_Id(employeeId).orElseThrow(() -> new
                RuntimeException("Employee  not found for employee ID: " + employeeId));

    }

    @Override
    public void deleteBankDetails(Long id) {
            repository.deleteById(id);
    }
}
