package com.meditourism.meditourism.blockedUser.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BlockedUserRequestDTO {

    @NotNull(message = "El ID del usuario no puede estar vacio")
    private Long userId;

    @NotBlank(message = "La razón del bloqueo no puede estar vacía")
    private String reason;

    public BlockedUserRequestDTO() {
    }

    public BlockedUserRequestDTO(Long userId, String reason) {
        this.userId = userId;
        this.reason = reason;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
