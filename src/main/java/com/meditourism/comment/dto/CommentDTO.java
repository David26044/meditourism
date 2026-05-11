package com.meditourism.comment.dto;

import com.meditourism.comment.entity.CommentEntity;
import com.meditourism.user.dto.UserResponseDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommentDTO {

    private Long id;

    @NotBlank(message = "El contenido no puede estar vacio")
    private String content;
    private Long fatherId;
    private UserResponseDTO user;
    @NotNull(message = "El id de la reseña no puede ser nulo")
    private Long reviewId;

    public CommentDTO(Long id, String content, Long fatherId, Long reviewId) {
        this.id = id;
        this.content = content;
        this.fatherId = fatherId;
        this.reviewId = reviewId;
    }

    public CommentDTO(){}

    public CommentDTO(CommentEntity entity){
        this.id = entity.getId();
        this.content = entity.getContent();
        this.fatherId = entity.getFatherCommentEntity() != null
                ? entity.getFatherCommentEntity().getId()
                : null;
        this.reviewId = entity.getReviewEntity().getId();
        this.user = new UserResponseDTO(entity.getUserEntity());
    }

    public UserResponseDTO getUser() {
        return user;
    }

    public void setUser(UserResponseDTO user) {
        this.user = user;
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

    public Long getFatherId() {
        return fatherId;
    }

    public void setFatherId(Long fatherId) {
        this.fatherId = fatherId;
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
