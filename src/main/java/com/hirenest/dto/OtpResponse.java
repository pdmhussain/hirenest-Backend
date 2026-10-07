package com.hirenest.dto;

public class OtpResponse {
    private String message;
    private String resetToken;
    public OtpResponse(String message) { this.message = message; }
    public OtpResponse(String message, String resetToken) { this.message = message; this.resetToken = resetToken; }
    public String getMessage() { return message; }
    public String getResetToken() { return resetToken; }
}
