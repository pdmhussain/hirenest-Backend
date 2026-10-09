package com.hirenest.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DomainRequest {

    private String name;

    private String description;

    private String status;
}