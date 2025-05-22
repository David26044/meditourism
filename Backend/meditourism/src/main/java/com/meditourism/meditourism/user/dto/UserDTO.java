package com.meditourism.meditourism.user.dto;

import com.meditourism.meditourism.role.entity.RoleEntity;
import jakarta.persistence.*;

public class UserDTO {

    private String email;
    private String name;
    private Long roleId;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
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
