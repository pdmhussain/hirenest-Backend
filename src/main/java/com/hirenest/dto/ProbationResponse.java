
package com.hirenest.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProbationResponse {

    private String employeeId;

    private LocalDate joiningDate;

    private Integer probationPeriod;

    private LocalDate probationStartDate;

    private LocalDate probationEndDate;

    private LocalDate currentDate;

    private String probationStatus;

    private String extensionReason;
}
