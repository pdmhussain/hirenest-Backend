package com.hirenest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecentRecordResponse {

    private Long employeeId;
    private String employeeCode;
    private String fullName;
    private String recordType;
    private LocalDate recordDate;
}