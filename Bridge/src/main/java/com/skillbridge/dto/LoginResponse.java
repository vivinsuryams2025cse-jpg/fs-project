package com.skillbridge.dto;

import com.skillbridge.enums.UserRole;

public class LoginResponse {

    private String accessToken;
    private String tokenType = "Bearer";
    private long expiresInMs;
    private Long studentId;
    private String fullName;
    private String email;
    private UserRole role;

    public LoginResponse() {
    }

    public LoginResponse(String accessToken, long expiresInMs, Long studentId, String fullName, String email, UserRole role) {
        this.accessToken = accessToken;
        this.expiresInMs = expiresInMs;
        this.studentId = studentId;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }

    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    public String getTokenType() { return tokenType; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }

    public long getExpiresInMs() { return expiresInMs; }
    public void setExpiresInMs(long expiresInMs) { this.expiresInMs = expiresInMs; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
}
