package com.hirenest.serviceImpl;

import com.hirenest.dto.PfDetailsDto;
import com.hirenest.entity.Employee;
import com.hirenest.entity.PFDetails;
import com.hirenest.repository.EmployeeRepository;
import com.hirenest.repository.PfDetailsRepository;
import com.hirenest.service.PfDetailsService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PfDetailsServiceImpl
        implements PfDetailsService {

    private final PfDetailsRepository pfDetailsRepository;

    private final EmployeeRepository employeeRepository;


    @Override
    public PFDetails createPfDetails(
            PfDetailsDto request,
            String email) {

        Employee employee = employeeRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employee not found with email: " + email));

        if (pfDetailsRepository.findByEmployee_Id(employee.getId()).isPresent()) {
            throw new RuntimeException(
                    "PF details already exist for this employee");
        }

        PFDetails pfDetails = new PFDetails();

        pfDetails.setEmployee(employee);
        pfDetails.setPfAccountNumber(request.getPfAccountNumber());
        pfDetails.setUan(request.getUan());
        pfDetails.setPfStatus(request.getPfStatus());
        pfDetails.setUanStatus(request.getUanStatus());

        return pfDetailsRepository.save(pfDetails);
    }

    @Override
    public List<PFDetails> getAllPfDetails() {

        return pfDetailsRepository.findAll();
    }


    @Override
    public PFDetails getByEmployeeId(
            Long employeeId) {

        return pfDetailsRepository
                .findByEmployee_Id(employeeId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "PF details not found for employee ID: "
                                        + employeeId
                        )
                );
    }
}