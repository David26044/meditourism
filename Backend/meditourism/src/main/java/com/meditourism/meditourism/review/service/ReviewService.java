package com.meditourism.meditourism.review.service;

import com.meditourism.meditourism.auth.service.AuthService;
import com.meditourism.meditourism.blockedUser.service.IBlockedUserService;
import com.meditourism.meditourism.clinic.entity.ClinicEntity;
import com.meditourism.meditourism.exception.ResourceNotFoundException;
import com.meditourism.meditourism.exception.UnauthorizedAccessException;
import com.meditourism.meditourism.review.dto.ReviewRequestDTO;
import com.meditourism.meditourism.review.dto.ReviewResponseDTO;
import com.meditourism.meditourism.review.entity.ReviewEntity;
import com.meditourism.meditourism.review.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService implements IReviewService {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    private AuthService authService;
    @Autowired
    private IBlockedUserService blockedUserService;

    /**
     * @return
     */
    @Override
    public List<ReviewResponseDTO> getAllReviews() {

        return ReviewResponseDTO.fromEntityList(reviewRepository.findAll());
    }

    /**
     * @param id
     * @return
     */
    @Override
    public ReviewResponseDTO getReviewById(Long id) {
        return new ReviewResponseDTO(reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la reseña con ID: " + id)));
    }

    /**
     * @param id
     * @return
     */
    @Override
    public List<ReviewResponseDTO> getReviewsByClinicId(Long id) {
        return ReviewResponseDTO.fromEntityList(reviewRepository.findAllByClinicId(id)
                .orElseThrow(() -> new ResourceNotFoundException("No hay reseñas asociadas a la clinica con ID: " + id)));
    }

    @Override
    public List<ReviewResponseDTO> getReviewsByUserId(Long id){
        return ReviewResponseDTO.fromEntityList(reviewRepository.findAllByUserId(id));
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
    public ReviewResponseDTO updateReview(Long id, ReviewRequestDTO dto) {
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
        return new ReviewResponseDTO(reviewRepository.save(review));
    }

    /**
     * @param dto
     * @return
     */
    @Override
    public ReviewResponseDTO saveReview(ReviewRequestDTO dto) {
        if (blockedUserService.isBlocked(dto.getUserId())) {
            throw new UnauthorizedAccessException("No tienes permisos");
        }
        ReviewEntity review = new ReviewEntity();
        //Setteo valores de la review
        ClinicEntity clinic = new ClinicEntity();
        clinic.setId(dto.getClinicId());

        //Contenido
        review.setContent(dto.getContent());

        //El service lanza la excepcion si el usuario no existe
        review.setUser(authService.getAuthenticatedUser());

        //El service de clinica lanza excepcion si la clinica no existe.
        review.setClinic(clinic);

        return new ReviewResponseDTO(reviewRepository.save(review));
    }

    /**
     * @param id
     */
    @Override
    public ReviewResponseDTO deleteReview(Long id) {
        ReviewEntity review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la reseña con ID: " + id));
        if (! authService.isOwnerOrAdmin(review.getUser().getId())) {
            throw new UnauthorizedAccessException("No tienes permiso para editar esta reseña");
        }
        reviewRepository.delete(review);
        return new ReviewResponseDTO(review);
    }

    @Override
    public List<ReviewResponseDTO> getLatestThreeReviews() {
        return ReviewResponseDTO.fromEntityList(reviewRepository.findTop3ByIdOrderByDesc());
    }

}
