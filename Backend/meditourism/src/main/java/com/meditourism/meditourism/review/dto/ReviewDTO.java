package com.meditourism.meditourism.review.dto;

public class ReviewDTO {

    private Long userId;
    private Long clinicId;
    private String content;
    private Integer rating;

    public ReviewDTO(Long userId, Long clinicId, String content, Integer rating) {
        this.userId = userId;
        this.clinicId = clinicId;
        this.content = content;
        this.rating = rating;
    }

    public ReviewDTO(){}

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
