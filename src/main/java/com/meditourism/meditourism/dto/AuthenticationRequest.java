package com.meditourism.meditourism.dto;

public class AuthenticationRequest {
    private String email;
    private String password;

    // Constructor vacío (requerido por Spring para deserializar)
    public AuthenticationRequest() {
    }

    // Constructor con parámetros (opcional)
    public AuthenticationRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters y setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

