package com.meditourism.meditourism.review.service;

import com.meditourism.meditourism.clinic.service.IClinicService;
import com.meditourism.meditourism.exception.ResourceNotFoundException;
import com.meditourism.meditourism.review.dto.ReviewDTO;
import com.meditourism.meditourism.review.entity.ReviewEntity;
import com.meditourism.meditourism.review.repository.ReviewRepository;
import com.meditourism.meditourism.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService implements IReviewService{

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    IUserService userService;

    @Autowired
    IClinicService clinicService;

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
     * @param dto
     * @return
     */
    @Override
    public ReviewEntity updateReview(ReviewDTO dto) {
        ReviewEntity review = reviewRepository.findById(dto.getReviewId())
                .orElseThrow(() -> new ResourceNotFoundException("No existe la reseña con ID: " + dto.getReviewId()));

        review.setContent(dto.getContent());
        return reviewRepository.save(review);
    }

    /**
     * @param dto
     * @return
     */
    @Override
    public ReviewEntity saveReview(ReviewDTO dto) {
        ReviewEntity review = new ReviewEntity();
        //Setteo valores de la review

        //Contenido
        review.setContent(dto.getContent());

        //El service lanza la excepcion si el usuario no existe
        review.setUser(userService
                .getUserById(dto.getUserId()));

        //El service de clinica lanza excepcion si la clinica no existe.
        review.setClinic(clinicService.getClinicById(dto.getClinicId()));

        return reviewRepository.save(review);
    }

    /**
     * @param id 
     */
    @Override
    public void deleteReview(Long id) {

    }
}
