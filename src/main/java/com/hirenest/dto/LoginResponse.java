
package com.hirenest.dto;

public class LoginResponse {

    private String token;
    private String email;
    private String fullName;
    private String role;
    private boolean mustChangePassword;

    public LoginResponse(
            String token,
            String email,
            String fullName,
            String role,
            boolean mustChangePassword) {
        this.token = token;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.mustChangePassword = mustChangePassword;
    }

    public String getToken() {
        return token;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getRole() {
        return role;
    }

    public boolean isMustChangePassword() {
        return mustChangePassword;
    }
}
