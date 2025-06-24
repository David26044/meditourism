package com.meditourism.meditourism.comment.service;

import com.meditourism.meditourism.auth.service.AuthService;
import com.meditourism.meditourism.blockedUser.service.BlockedUserService;
import com.meditourism.meditourism.comment.dto.CommentDTO;
import com.meditourism.meditourism.comment.entity.CommentEntity;
import com.meditourism.meditourism.comment.repository.CommentRepository;
import com.meditourism.meditourism.exception.ResourceNotFoundException;
import com.meditourism.meditourism.exception.UnauthorizedAccessException;
import com.meditourism.meditourism.notification.dto.NotificationRequestDTO;
import com.meditourism.meditourism.notification.service.INotificationService;
import com.meditourism.meditourism.review.entity.ReviewEntity;
import com.meditourism.meditourism.review.repository.ReviewRepository;
import com.meditourism.meditourism.user.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio para la gestión de comentarios en el sistema.
 * Proporciona funcionalidades para crear, leer, actualizar y eliminar comentarios,
 * así como para manejar respuestas a comentarios y notificaciones relacionadas.
 */
@Service
public class CommentService implements ICommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private BlockedUserService blockedUserService;

    @Autowired
    private INotificationService notificationService;

    @Autowired
    private ReviewRepository reviewRepository;

    /**
     * Obtiene todos los comentarios del sistema.
     *
     * @return Lista de todos los comentarios en formato DTO
     */
    @Override
    public List<CommentDTO> getAllComments() {
        return CommentDTO.fromEntityList(commentRepository.findAll());
    }

    /**
     * Obtiene todos los comentarios asociados a una reseña específica.
     *
     * @param id ID de la reseña
     * @return Lista de comentarios de la reseña
     * @throws ResourceNotFoundException si no se encuentran comentarios para la reseña
     */
    @Override
    public List<CommentDTO> getCommentsByReviewId(Long id) {
        return CommentDTO.fromEntityList(commentRepository.findAllByReviewEntityId(id)
                .orElseThrow(() -> new ResourceNotFoundException("No hay comentarios asociados a la reseña: " + id)));
    }

    /**
     * Obtiene un comentario específico por su ID.
     *
     * @param id ID del comentario
     * @return DTO con los datos del comentario
     * @throws ResourceNotFoundException si no se encuentra el comentario
     */
    public CommentDTO getCommentById(Long id) {
        return new CommentDTO(commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el comentario con ID: " + id)));
    }

    /**
     * Obtiene todos los comentarios realizados por un usuario específico.
     *
     * @param userId ID del usuario
     * @return Lista de comentarios del usuario
     */
    public List<CommentDTO> getCommentsByUserId(Long userId) {
        return CommentDTO.fromEntityList(commentRepository.findAllByUserEntityId(userId));
    }

    /**
     * Obtiene la entidad de un comentario por su ID.
     *
     * @param id ID del comentario
     * @return Entidad del comentario
     * @throws ResourceNotFoundException si no se encuentra el comentario
     */
    @Override
    public CommentEntity getCommentEntityById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el comentario con ID: " + id));
    }

    /**
     * Guarda un nuevo comentario en el sistema.
     *
     * @param dto DTO con los datos del comentario a guardar
     * @return DTO con los datos del comentario guardado
     * @throws UnauthorizedAccessException si el usuario está bloqueado
     * @throws ResourceNotFoundException si no se encuentra la reseña o comentario padre
     */
    @Override
    public CommentDTO saveComment(CommentDTO dto) {
        Long currentUserId = authService.getAuthenticatedUser().getId();

        if (blockedUserService.isBlocked(currentUserId)) {
            throw new UnauthorizedAccessException("No tienes permisos");
        }

        CommentEntity entity = new CommentEntity();
        ReviewEntity reviewEntity = new ReviewEntity();
        reviewEntity.setId(dto.getReviewId());

        entity.setContent(dto.getContent());
        entity.setReviewEntity(reviewEntity);
        entity.setUserEntity(authService.getAuthenticatedUser());

        // Verifica si es respuesta a otro comentario
        if (dto.getFatherId() != null) {
            CommentEntity father = getCommentEntityById(dto.getFatherId());
            entity.setFatherCommentEntity(father);

            // Notificar al autor del comentario padre
            if (!father.getUserEntity().getId().equals(currentUserId)) {
                notificationService.saveNotification(
                        new NotificationRequestDTO(
                                "Nuevo comentario a tu comentario",
                                "El usuario " + authService.getAuthenticatedUser().getName() + " respondió a tu comentario.",
                                father.getUserEntity().getId()
                        )
                );
            }
        } else {
            // Es comentario directo a una reseña
            ReviewEntity review = reviewRepository.findById(dto.getReviewId())
                    .orElseThrow(() -> new ResourceNotFoundException("No existe la reseña con ID: " + dto.getReviewId()));

            if (!review.getUser().getId().equals(currentUserId)) {
                notificationService.saveNotification(
                        new NotificationRequestDTO(
                                "Nuevo comentario en tu reseña",
                                "El usuario " + authService.getAuthenticatedUser().getName() + " comentó en tu reseña.",
                                review.getUser().getId()
                        )
                );
            }
        }

        return new CommentDTO(commentRepository.save(entity));
    }

    /**
     * Elimina un comentario del sistema.
     *
     * @param id ID del comentario a eliminar
     * @return DTO con los datos del comentario eliminado
     * @throws UnauthorizedAccessException si el usuario no tiene permisos
     * @throws ResourceNotFoundException si no se encuentra el comentario
     */
    @Override
    public CommentDTO deleteComment(Long id) {
        CommentEntity entity = getCommentEntityById(id);

        if (!authService.isOwnerOrAdmin(entity.getUserEntity().getId())) {
            throw new UnauthorizedAccessException("No tienes permiso para eliminar el comentario");
        }
        commentRepository.delete(entity);
        return new CommentDTO(entity);
    }

    /**
     * Actualiza el contenido de un comentario existente.
     *
     * @param id ID del comentario a actualizar
     * @param dto DTO con el nuevo contenido
     * @return DTO con los datos del comentario actualizado
     * @throws UnauthorizedAccessException si el usuario no tiene permisos
     * @throws ResourceNotFoundException si no se encuentra el comentario
     */
    @Override
    public CommentDTO updateComment(Long id, CommentDTO dto) {
        CommentEntity entity = getCommentEntityById(id);
        if (!authService.isOwnerOrAdmin(entity.getUserEntity().getId())) {
            throw new UnauthorizedAccessException("No tienes permiso para editar el comentario");
        }
        if (dto.getContent() != null) {
            entity.setContent(dto.getContent());
        }
        return new CommentDTO(commentRepository.save(entity));
    }

    /**
     * Obtiene todas las respuestas a un comentario específico.
     *
     * @param id ID del comentario padre
     * @return Lista de respuestas al comentario
     * @throws ResourceNotFoundException si no se encuentra el comentario padre
     */
    @Override
    public List<CommentDTO> getRepliesByCommentId(Long id) {
        return CommentDTO.fromEntityList(commentRepository.findAllByFatherCommentEntityId(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el comentario con ID: " + id)));
    }
}