package com.meditourism.meditourism.user.dto;

import com.meditourism.meditourism.role.entity.RoleEntity;
import com.meditourism.meditourism.user.entity.UserEntity;

public class UserResponseDTO {
    private Long id;
    private String email;
    private String name;
    private boolean isVerified;
    private RoleEntity role;
    private java.time.LocalDateTime createdAt;

    public UserResponseDTO(UserEntity user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.isVerified = user.isVerified();
        this.role = user.getRoleEntity();
        this.createdAt = user.getCreatedAt();
    }

    public UserResponseDTO(Long id, String email, String name, boolean isVerified, RoleEntity role) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.isVerified = isVerified;
        this.role = role;
    }

    public UserResponseDTO(){}

    public RoleEntity getRole() {
        return role;
    }

    public void setRole(RoleEntity role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public java.time.LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.time.LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

