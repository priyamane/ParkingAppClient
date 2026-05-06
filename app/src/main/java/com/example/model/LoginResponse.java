package com.example.model;

import com.google.gson.annotations.SerializedName; // Import this!

public class LoginResponse {

    // 1. Tell Gson to look for "token" in the JSON and put it here
    @SerializedName("token")
    private String jwtToken;

    // 2. Tell Gson to look for "role" (or "userRole" check your server!)
    @SerializedName("role")
    private String role;

    // --- GETTERS ---
    public String getJwtToken() {
        return jwtToken;
    }

    public String getRole() {
        return role;
    }
}