package com.hirenest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class VerifyRegistrationOtpRequest {
    @Email @NotBlank private String email;
    @NotBlank @Pattern(regexp="\\d{6}") private String otp;
    public String getEmail() { return email; }
    public String getOtp() { return otp; }
    public void setEmail(String email) { this.email = email; }
    public void setOtp(String otp) { this.otp = otp; }
}
