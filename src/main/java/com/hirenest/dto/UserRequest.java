package com.hirenest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {

    private String username;
    private String email;
    private String password;
    private String role;
    private String status;
    private boolean active;
    private boolean emailVerified;
}