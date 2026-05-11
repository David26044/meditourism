package com.meditourism.auth.dto;

import jakarta.validation.constraints.NotBlank;

public class ChangePasswordDTO {

    @NotBlank(message = "La nueva contraseña no puede estar vacia")
    private String newPassword;

    public ChangePasswordDTO() {
    }

    public ChangePasswordDTO(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
