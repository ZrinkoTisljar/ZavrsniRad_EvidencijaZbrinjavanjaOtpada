package com.example.wasteapp.auth;

public class AuthResponse {
    private String token;
    private String email;
    private String role;
    private String userType;

    public AuthResponse(String token, String email, String role, String userType) {
        this.token = token;
        this.email = email;
        this.role = role;
        this.userType = userType;
    }

    public String getToken() { return token; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getUserType() { return userType; }
}
