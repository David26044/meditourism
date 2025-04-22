package com.meditourism.meditourism.dto;

public class JwtResponse {

    private String token;
    private String tokenType = "Bearer"; // Por convención, los tokens JWT se envían con el prefijo "Bearer"

    // Constructor
    public JwtResponse(String token) {
        this.token = token;
    }

    // Getters y Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
