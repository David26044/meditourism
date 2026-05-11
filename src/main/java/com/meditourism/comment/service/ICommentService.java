package com.meditourism.comment.service;

import com.meditourism.comment.dto.CommentDTO;
import com.meditourism.comment.entity.CommentEntity;

import java.util.List;

public interface ICommentService {
    List<CommentDTO> getAllComments();
    List<CommentDTO> getCommentsByReviewId(Long reviewId);
    CommentEntity getCommentEntityById(Long id);
    CommentDTO saveComment(CommentDTO dto);
    CommentDTO deleteComment(Long id);
    CommentDTO updateComment(Long id, CommentDTO dto);
    List<CommentDTO> getRepliesByCommentId(Long commentId);
    CommentDTO getCommentById(Long id);
}
