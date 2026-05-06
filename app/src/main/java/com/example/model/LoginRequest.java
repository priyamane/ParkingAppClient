package com.example.model;

public class LoginRequest {
    private final String username;
    private final String password;
    private final String branchId; // <-- THIS MUST BE PRESENT

    public LoginRequest(String username, String password, String branchId) {
        this.username = username;
        this.password = password;
        this.branchId = branchId;
    }
}