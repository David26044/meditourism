package com.meditourism.meditourism.review.service;

import com.meditourism.meditourism.auth.service.AuthService;
import com.meditourism.meditourism.clinic.service.IClinicService;
import com.meditourism.meditourism.exception.ResourceNotFoundException;
import com.meditourism.meditourism.exception.UnauthorizedAccessException;
import com.meditourism.meditourism.review.dto.ReviewDTO;
import com.meditourism.meditourism.review.entity.ReviewEntity;
import com.meditourism.meditourism.review.repository.ReviewRepository;
import com.meditourism.meditourism.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService implements IReviewService {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    IUserService userService;

    @Autowired
    IClinicService clinicService;
    @Autowired
    private AuthService authService;

    /**
     * @return
     */
    @Override
    public List<ReviewDTO> getAllReviews() {

        return ReviewDTO.fromEntityList(reviewRepository.findAll());
    }

    /**
     * @param id
     * @return
     */
    @Override
    public ReviewDTO getReviewById(Long id) {
        return new ReviewDTO(reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la reseña con ID: " + id)));
    }

    /**
     * @param id
     * @return
     */
    @Override
    public List<ReviewDTO> getReviewsByClinicId(Long id) {
        return ReviewDTO.fromEntityList(reviewRepository.findAllByClinicId(id)
                .orElseThrow(() -> new ResourceNotFoundException("No hay reseñas asociadas a la clinica con ID: " + id)));
    }

    @Override
    public ReviewEntity getReviewEntityById(Long id){
        return reviewRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No existe la reseña con ID: " + id));
    }

    /**
     * @param dto
     * @return
     */
    @Override
    public ReviewDTO updateReview(Long id, ReviewDTO dto) {
        ReviewEntity review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la reseña con ID: " + id));

        if (! authService.isOwnerOrAdmin(review.getUser().getId())) {
            throw new UnauthorizedAccessException("No tienes permiso para editar esta reseña");
        }

        if (dto.getRating() != 0) {
            review.setRating(dto.getRating());
        }
        if (dto.getContent() != null) {
            review.setContent(dto.getContent());
        }
        return new ReviewDTO(reviewRepository.save(review));
    }

    /**
     * @param dto
     * @return
     */
    @Override
    public ReviewDTO saveReview(ReviewDTO dto) {
        ReviewEntity review = new ReviewEntity();
        //Setteo valores de la review

        //Contenido
        review.setContent(dto.getContent());

        //El service lanza la excepcion si el usuario no existe
        review.setUser(userService
                .getUserEntityById(dto.getUserId()));

        //El service de clinica lanza excepcion si la clinica no existe.
        review.setClinic(clinicService.getClinicEntityById(dto.getClinicId()));

        return new ReviewDTO(reviewRepository.save(review));
    }

    /**
     * @param id
     */
    @Override
    public ReviewDTO deleteReview(Long id) {
        ReviewEntity review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la reseña con ID: " + id));
        if (! authService.isOwnerOrAdmin(review.getUser().getId())) {
            throw new UnauthorizedAccessException("No tienes permiso para editar esta reseña");
        }
        reviewRepository.delete(review);
        return new ReviewDTO(review);
    }

}
