package com.meditourism.meditourism.service;

import com.meditourism.meditourism.entity.ReviewEntity;

import java.util.List;

public interface ReviewService {
    List<ReviewEntity> getAllReviews();
    ReviewEntity getReviewById(Long id);
    ReviewEntity saveReview(ReviewEntity review);
    void deleteReview(Long id);
}
