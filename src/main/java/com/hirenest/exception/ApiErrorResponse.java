package com.hirenest.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ApiErrorResponse {

    private int status;
    private String message;
    private LocalDateTime timestamp;


    public ApiErrorResponse() {
    }


    public ApiErrorResponse(
            int status,
            String message,
            LocalDateTime timestamp) {

        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

}