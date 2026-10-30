package com.hirenest.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProbationDomainSummaryResponse {

    private Long domainId;

    private String domainName;

    private long totalEmployees;

    private long probationCompleted;

    private long probationInProgress;
}
