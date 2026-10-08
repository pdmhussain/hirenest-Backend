package com.hirenest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ResetPasswordRequest {
    @Email @NotBlank private String email;
    @NotBlank private String resetToken;
    @NotBlank @Size(min = 8, max = 100) private String newPassword;
    public String getEmail() { return email; }
    public String getResetToken() { return resetToken; }
    public String getNewPassword() { return newPassword; }
    public void setEmail(String email) { this.email = email; }
    public void setResetToken(String resetToken) { this.resetToken = resetToken; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}
