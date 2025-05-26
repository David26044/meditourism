package com.meditourism.meditourism.comment.dto;

import com.meditourism.meditourism.comment.entity.CommentEntity;
import com.meditourism.meditourism.review.dto.ReviewDTO;
import com.meditourism.meditourism.review.entity.ReviewEntity;
import com.meditourism.meditourism.user.dto.UserDTO;
import com.meditourism.meditourism.user.entity.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommentDTO {

    private Long id;

    @NotBlank(message = "El contenido no puede estar vacio")
    private String content;
    private LocalDateTime date;
    private Long fatherId;
    @NotBlank(message = "El id de usuario no puede ser nulo")
    private Long userId;
    @NotBlank(message = "El id de la reseña no puede ser nula")
    private Long reviewId;

    public CommentDTO(Long id, String content, LocalDateTime date, Long fatherId, Long userId, Long reviewId) {
        this.id = id;
        this.content = content;
        this.date = date;
        this.fatherId = fatherId;
        this.userId = userId;
        this.reviewId = reviewId;
    }

    public CommentDTO(){}

    public CommentDTO(CommentEntity entity){
        this.id = entity.getId();
        this.content = entity.getContent();
        this.date = entity.getDate();
        this.fatherId = entity.getFatherCommentEntity().getId();
        this.userId = entity.getUserEntity().getId();
        this.reviewId = entity.getReviewEntity().getId();
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Long getFatherId() {
        return fatherId;
    }

    public void setFatherId(Long fatherId) {
        this.fatherId = fatherId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public static List<CommentDTO> fromEntityList(List<CommentEntity> entities) {
        List<CommentDTO> dtoList = new ArrayList<>();
        for (CommentEntity entity : entities) {
            dtoList.add(new CommentDTO(entity));
        }
        return dtoList;
    }
}
