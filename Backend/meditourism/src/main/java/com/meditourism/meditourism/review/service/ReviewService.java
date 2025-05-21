package com.meditourism.meditourism.review.service;

import com.meditourism.meditourism.exception.ResourceNotFoundException;
import com.meditourism.meditourism.review.entity.ReviewEntity;
import com.meditourism.meditourism.review.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService implements IReviewService{

    @Autowired
    ReviewRepository reviewRepository;

    /**
     * @return 
     */
    @Override
    public List<ReviewEntity> getAllReviews() {
        return reviewRepository.findAll();
    }

    /**
     * @param id 
     * @return
     */
    @Override
    public ReviewEntity getReviewById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la reseña con ID: " + id));
    }

    /**
     * @param id 
     * @return
     */
    @Override
    public List<ReviewEntity> getReviewByClinicId(Long id) {
        return reviewRepository.findByClinicEntityId(id)
                .orElseThrow(() -> new ResourceNotFoundException("No hay reseñas asociadas a la clinica con ID: "+ id));
    }

    /**
     * @param review 
     * @return
     */
    @Override
    public ReviewEntity updateReview(ReviewEntity review) {
        return null;
    }

    /**
     * @param review 
     * @return
     */
    @Override
    public ReviewEntity saveReview(ReviewEntity review) {
        return null;
    }

    /**
     * @param id 
     */
    @Override
    public void deleteReview(Long id) {

    }
}
