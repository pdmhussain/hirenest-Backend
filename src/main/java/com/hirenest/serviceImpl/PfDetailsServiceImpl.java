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
public class PfDetailsServiceImpl implements PfDetailsService {
    private final PfDetailsRepository repository;
    private final EmployeeRepository employeeRepository;

    @Override
    public PFDetails createPfDetails(PfDetailsDto request) {
        Employee employee = employeeRepository
                .findById(request.getEmployeeId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employee not found with ID: "
                                        + request.getEmployeeId()
                        )
                );

        PFDetails pfDetails = new PFDetails();

        pfDetails.setEmployee(employee);
        pfDetails.setPfAccountNumber(request.getPfAccountNumber());
        pfDetails.setUan(request.getUan());
        pfDetails.setPfStatus(request.getPfStatus());
        pfDetails.setUanStatus(request.getUanStatus());

        return repository.save(pfDetails);
    }

    @Override
    public List<PFDetails> getAllPfDetails() {
        return repository.findAll();
    }

    @Override
    public PFDetails getByEmployeeId(Long employeeId) {

        return repository.findByEmployee_Id(employeeId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "PF details not found for employee ID: " + employeeId
                        )
                );
    }

    @Override
    public void deletePfDetails(Long id) {
        repository.deleteById(id);
    }
}
