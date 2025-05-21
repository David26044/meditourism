package com.meditourism.meditourism.review.service;

import com.meditourism.meditourism.review.entity.ReviewEntity;

import java.util.List;

public interface IReviewService {
    List<ReviewEntity> getAllReviews();
    ReviewEntity getReviewById(Long id);
    List<ReviewEntity> getReviewByClinicId(Long id);
    ReviewEntity updateReview(ReviewEntity review);
    ReviewEntity saveReview(ReviewEntity review);
    void deleteReview(Long id);
}
