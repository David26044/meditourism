package com.meditourism.meditourism.review.service;

import com.meditourism.meditourism.review.dto.ReviewDTO;
import com.meditourism.meditourism.review.entity.ReviewEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface IReviewService {
    List<ReviewDTO> getAllReviews();
    ReviewDTO getReviewById(Long id);
    List<ReviewDTO> getReviewsByClinicId(Long id);
    ReviewEntity getReviewEntityById(Long id);
    ReviewDTO updateReview(Long id, ReviewDTO dto);
    ReviewDTO saveReview(ReviewDTO review);
    ReviewDTO deleteReview(Long id);
}
