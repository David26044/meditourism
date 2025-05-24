package com.meditourism.meditourism.user.dto;

import com.meditourism.meditourism.role.entity.RoleEntity;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserDTO {

    @NotBlank(message = "El correo no puede estar vacío")
    @Email(message = "El correo no tiene un formato valido")
    private String email;

    @NotBlank(message = "El nomre no puede estar vacio")
    private String name;

    @NotBlank(message = "La clave no puede estar vacia")
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
