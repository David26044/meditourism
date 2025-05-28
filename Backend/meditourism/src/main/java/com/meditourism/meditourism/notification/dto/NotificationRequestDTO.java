package com.meditourism.meditourism.notification.dto;

import com.meditourism.meditourism.user.entity.UserEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class NotificationRequestDTO {

    @NotBlank(message = "El titulo no puede estar vacio")
    private String title;
    @NotBlank(message = "El contenido no puede estar vacio")
    private String content;
    @NotNull(message = "El id de usuario no puede estar vacío")
    private Long userId;

    public NotificationRequestDTO() {

    }
    public NotificationRequestDTO(String content, Long userId) {
        this.content = content;
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
