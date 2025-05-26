package com.meditourism.meditourism.review.dto;

import com.meditourism.meditourism.clinic.dto.ClinicDTO;
import com.meditourism.meditourism.clinic.entity.ClinicEntity;
import com.meditourism.meditourism.review.entity.ReviewEntity;
import com.meditourism.meditourism.user.dto.UserResponseDTO;
import com.meditourism.meditourism.user.entity.UserEntity;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReviewResponseDTO {

    private Long id;
    private UserResponseDTO user;
    private ClinicDTO clinic;
    private String content;
    private LocalDateTime date;
    private Integer rating;

    public ReviewResponseDTO() {
    }

    public ReviewResponseDTO(Long id, UserResponseDTO user, ClinicDTO clinic, String content, LocalDateTime date, Integer rating) {
        this.id = id;
        this.user = user;
        this.clinic = clinic;
        this.content = content;
        this.date = date;
        this.rating = rating;
    }

    public ReviewResponseDTO(ReviewEntity entity) {
        this.id = entity.getId();
        this.user = new UserResponseDTO(entity.getUser());
        this.clinic = new ClinicDTO(entity.getClinic());
        this.content = entity.getContent();
        this.date = entity.getDate();
        this.rating = entity.getRating();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserResponseDTO getUser() {
        return user;
    }

    public void setUser(UserResponseDTO user) {
        this.user = user;
    }

    public ClinicDTO getClinic() {
        return clinic;
    }

    public void setClinic(ClinicDTO clinic) {
        this.clinic = clinic;
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

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public static List<ReviewResponseDTO> fromEntityList(List<ReviewEntity> entities) {
        List<ReviewResponseDTO> dtoList = new ArrayList<>();
        for (ReviewEntity entity : entities) {
            dtoList.add(new ReviewResponseDTO(entity));
        }
        return dtoList;
    }

}
