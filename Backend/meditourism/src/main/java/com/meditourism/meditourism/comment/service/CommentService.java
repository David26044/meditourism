package com.meditourism.meditourism.comment.service;

import com.meditourism.meditourism.auth.service.AuthService;
import com.meditourism.meditourism.comment.dto.CommentDTO;
import com.meditourism.meditourism.comment.entity.CommentEntity;
import com.meditourism.meditourism.comment.repository.CommentRepository;
import com.meditourism.meditourism.exception.ResourceNotFoundException;
import com.meditourism.meditourism.exception.UnauthorizedAccessException;
import com.meditourism.meditourism.review.service.ReviewService;
import com.meditourism.meditourism.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService implements ICommentService{

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;

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

    @Override
    public CommentEntity getCommentEntityById(Long id){
        return commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el comentario con ID: " + id));
    }

    /**
     * @param dto
     */
    @Override
    public CommentDTO saveComment(CommentDTO dto) {
        CommentEntity entity = new CommentEntity();
        entity.setContent(dto.getContent());
        entity.setFatherCommentEntity(getCommentEntityById(dto.getFatherId()));
        entity.setReviewEntity(reviewService.getReviewEntityById(dto.getReviewId()));
        entity.setUserEntity(userService.getUserEntityById(dto.getUserId()));
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
