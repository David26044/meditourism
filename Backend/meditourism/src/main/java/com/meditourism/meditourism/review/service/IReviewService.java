package com.meditourism.meditourism.review.service;

import com.meditourism.meditourism.review.dto.ReviewRequestDTO;
import com.meditourism.meditourism.review.dto.ReviewResponseDTO;
import com.meditourism.meditourism.review.entity.ReviewEntity;

import java.util.List;

public interface IReviewService {
    List<ReviewResponseDTO> getAllReviews();
    ReviewResponseDTO getReviewById(Long id);
    List<ReviewResponseDTO> getReviewsByClinicId(Long id);
    ReviewEntity getReviewEntityById(Long id);
    ReviewResponseDTO updateReview(Long id, ReviewRequestDTO dto);
    ReviewResponseDTO saveReview(ReviewRequestDTO review);
    ReviewResponseDTO deleteReview(Long id);
}
