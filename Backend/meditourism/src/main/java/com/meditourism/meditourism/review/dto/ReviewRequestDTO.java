package com.meditourism.meditourism.review.dto;

import com.meditourism.meditourism.review.entity.ReviewEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ReviewRequestDTO {

    private Long id;

    @NotNull(message = "El ID del usuario no puede ser nulo")
    private Long userId;

    @NotNull(message = "El ID de la clínica no puede ser nulo")
    private Long clinicId;

    @NotBlank(message = "El contenido no puede estar vacío")
    private String content;

    @NotNull(message = "La calificación no puede ser nula")
    private Integer rating;

    public ReviewRequestDTO(Long id, Long userId, Long clinicId, String content, Integer rating) {
        this.id = id;
        this.userId = userId;
        this.clinicId = clinicId;
        this.content = content;
        this.rating = rating;
    }

    public ReviewRequestDTO(){}

    public ReviewRequestDTO(ReviewEntity entity){
        this.id = entity.getId();
        this.userId = entity.getUser().getId();
        this.clinicId = entity.getClinic().getId();
        this.content = entity.getContent();
        this.rating = entity.getRating();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getClinicId() {
        return clinicId;
    }

    public void setClinicId(Long clinicId) {
        this.clinicId = clinicId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }


}
