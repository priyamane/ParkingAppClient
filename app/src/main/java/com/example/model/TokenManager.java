// File: TokenManager.java
package com.example.model;

public class TokenManager {
    // This static variable will hold the JWT received after successful login
    private static String jwtToken;

    public static String getToken() {
        // Returns the stored token for use in API requests
        return jwtToken;
    }

    public static void setToken(String token) {
        // Used by LoginActivity to store the token received from the backend
        jwtToken = token;
    }
}