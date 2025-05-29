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

/**
 * Servicio para gestionar las operaciones relacionadas con las reseñas.
 */
@Service
public class ReviewService implements IReviewService {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    private AuthService authService;
    @Autowired
    private IBlockedUserService blockedUserService;

    /**
     * Obtiene todas las reseñas existentes.
     *
     * @return Lista de todas las reseñas en formato DTO
     */
    @Override
    public List<ReviewResponseDTO> getAllReviews() {
        return ReviewResponseDTO.fromEntityList(reviewRepository.findAll());
    }

    /**
     * Obtiene una reseña específica por su ID.
     *
     * @param id ID de la reseña a buscar
     * @return Reseña encontrada en formato DTO
     * @throws ResourceNotFoundException Si no se encuentra la reseña
     */
    @Override
    public ReviewResponseDTO getReviewById(Long id) {
        return new ReviewResponseDTO(reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la reseña con ID: " + id)));
    }

    /**
     * Obtiene todas las reseñas asociadas a una clínica específica.
     *
     * @param id ID de la clínica
     * @return Lista de reseñas de la clínica en formato DTO
     * @throws ResourceNotFoundException Si no hay reseñas para la clínica
     */
    @Override
    public List<ReviewResponseDTO> getReviewsByClinicId(Long id) {
        return ReviewResponseDTO.fromEntityList(reviewRepository.findAllByClinicId(id)
                .orElseThrow(() -> new ResourceNotFoundException("No hay reseñas asociadas a la clinica con ID: " + id)));
    }

    /**
     * Obtiene todas las reseñas realizadas por un usuario específico.
     *
     * @param id ID del usuario
     * @return Lista de reseñas del usuario en formato DTO
     */
    @Override
    public List<ReviewResponseDTO> getReviewsByUserId(Long id) {
        return ReviewResponseDTO.fromEntityList(reviewRepository.findAllByUserId(id));
    }

    /**
     * Obtiene la entidad de reseña por su ID.
     *
     * @param id ID de la reseña
     * @return Entidad de la reseña
     * @throws ResourceNotFoundException Si no se encuentra la reseña
     */
    @Override
    public ReviewEntity getReviewEntityById(Long id) {
        return reviewRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No existe la reseña con ID: " + id));
    }

    /**
     * Actualiza una reseña existente.
     *
     * @param id ID de la reseña a actualizar
     * @param dto Datos para actualizar la reseña
     * @return Reseña actualizada en formato DTO
     * @throws ResourceNotFoundException Si no se encuentra la reseña
     * @throws UnauthorizedAccessException Si el usuario no tiene permisos para editar
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
     * Guarda una nueva reseña.
     *
     * @param dto Datos de la nueva reseña
     * @return Reseña creada en formato DTO
     * @throws UnauthorizedAccessException Si el usuario está bloqueado
     */
    @Override
    public ReviewResponseDTO saveReview(ReviewRequestDTO dto) {
        // Remove userId check since we get user from authentication
        if (blockedUserService.isBlocked(authService.getAuthenticatedUser().getId())) {
            throw new UnauthorizedAccessException("No tienes permisos");
        }

        ReviewEntity review = new ReviewEntity();

        // Crear referencia a la clínica
        ClinicEntity clinic = new ClinicEntity();
        clinic.setId(dto.getClinicId());

        // Establecer todos los campos
        review.setContent(dto.getContent());
        review.setRating(dto.getRating());
        review.setUser(authService.getAuthenticatedUser());
        review.setClinic(clinic);

        return new ReviewResponseDTO(reviewRepository.save(review));
    }

    /**
     * Elimina una reseña existente.
     *
     * @param id ID de la reseña a eliminar
     * @return Reseña eliminada en formato DTO
     * @throws ResourceNotFoundException Si no se encuentra la reseña
     * @throws UnauthorizedAccessException Si el usuario no tiene permisos para eliminar
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

    /**
     * Obtiene las últimas tres reseñas creadas.
     *
     * @return Lista de las últimas tres reseñas en formato DTO
     */
    @Override
    public List<ReviewResponseDTO> getLatestThreeReviews() {
        return ReviewResponseDTO.fromEntityList(reviewRepository.findTop3ByIdOrderByDesc());
    }
}
