package com.hirenest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DomainResponse {

    private Long id;
    private String name;
    private String description;
    private String status;
}
