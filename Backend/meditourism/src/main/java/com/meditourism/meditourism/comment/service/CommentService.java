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

@Service
public class CommentService implements ICommentService{

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private BlockedUserService blockedUserService;
    @Autowired
    private INotificationService notificationService;
    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public List<CommentDTO> getAllComments(){
        return CommentDTO.fromEntityList(commentRepository.findAll());
    }

    /**
     * @param id
     * @return
     */
    @Override
    public List<CommentDTO> getCommentsByReviewId(Long id) {
        return CommentDTO.fromEntityList(commentRepository.findAllByReviewEntityId(id)
                .orElseThrow(() -> new ResourceNotFoundException("No hay comentarios asociados a la reseña: " +id)));
    }

    public CommentDTO getCommentById(Long id){
        return new CommentDTO(commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el comentario con ID: "+id)));
    }

    public List<CommentDTO> getCommentsByUserId(Long userId){
        return CommentDTO.fromEntityList(commentRepository.findAllByUserEntityId(userId));
    }

    @Override
    public CommentEntity getCommentEntityById(Long id){
        return commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el comentario con ID: " + id));
    }

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
     * @param id 
     */
    @Override
    public CommentDTO deleteComment(Long id) {
        CommentEntity entity = getCommentEntityById(id);

        if (authService.isOwnerOrAdmin(entity.getUserEntity().getId())) {
            throw new UnauthorizedAccessException("No tienes permiso para eliminar el comentario");
        }
        commentRepository.delete(entity);
        return new CommentDTO(entity);
    }

    /**
     * @param id, comment
     */
    @Override
    public CommentDTO updateComment(Long id, CommentDTO dto) {
        CommentEntity entity = getCommentEntityById(id);
        if (authService.isOwnerOrAdmin(entity.getUserEntity().getId())) {
            throw new UnauthorizedAccessException("No tienes permiso para editar el comentario");
        }
        if (dto.getContent() != null) {
            entity.setContent(dto.getContent());
        }
        return new CommentDTO(commentRepository.save(entity));
    }

    /**
     * @param id
     * @return
     */
    @Override
    public List<CommentDTO> getRepliesByCommentId(Long id) {
        return CommentDTO.fromEntityList(commentRepository.findAllByFatherCommentEntityId(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el comentario con ID: " + id)));
    }

}
