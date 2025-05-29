package com.meditourism.meditourism.review.dto;

import com.meditourism.meditourism.review.entity.ReviewEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReviewRequestDTO {

    private Long id;

    @NotNull(message = "El ID de la clínica no puede ser nulo")
    private Long clinicId;

    @NotBlank(message = "El contenido no puede estar vacío")
    private String content;

    @NotNull(message = "La calificación no puede ser nula")
    private Integer rating;

    public ReviewRequestDTO(Long id, Long clinicId, String content, Integer rating) {
        this.id = id;
        this.clinicId = clinicId;
        this.content = content;
        this.rating = rating;
    }

    public ReviewRequestDTO(){}

    public ReviewRequestDTO(ReviewEntity entity){
        this.id = entity.getId();
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
