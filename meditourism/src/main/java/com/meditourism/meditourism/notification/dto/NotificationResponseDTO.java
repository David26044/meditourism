package com.meditourism.meditourism.notification.dto;

import com.meditourism.meditourism.notification.entity.NotificationEntity;
import com.meditourism.meditourism.review.dto.ReviewResponseDTO;
import com.meditourism.meditourism.review.entity.ReviewEntity;
import com.meditourism.meditourism.user.dto.UserResponseDTO;

import javax.management.Notification;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificationResponseDTO {

    private Long id;
    private String content;
    private UserResponseDTO user;
    private LocalDateTime date;

    public NotificationResponseDTO(Long id, String content, UserResponseDTO user, LocalDateTime date) {
        this.id = id;
        this.content = content;
        this.user = user;
        this.date = date;
    }
    public NotificationResponseDTO() {}

    public NotificationResponseDTO(NotificationEntity entity) {
        this.id = entity.getId();
        this.content = entity.getContent();
        this.user = new UserResponseDTO(entity.getUser());
        this.date = entity.getDate();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserResponseDTO getUser() {
        return user;
    }

    public void setUser(UserResponseDTO user) {
        this.user = user;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public static List<NotificationResponseDTO> fromEntityList(List<NotificationEntity> entities) {
        List<NotificationResponseDTO> dtoList = new ArrayList<>();
        for (NotificationEntity entity : entities) {
            dtoList.add(new NotificationResponseDTO(entity));
        }
        return dtoList;
    }

}
