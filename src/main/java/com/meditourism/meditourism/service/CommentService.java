package com.meditourism.meditourism.service;

import com.meditourism.meditourism.entity.CommentEntity;

import java.util.List;

public interface CommentService {
    List<CommentEntity> getCommentsByReviewId(Long reviewId);
    void saveComment(CommentEntity comment);
    void deleteComment(Long id);
    void updateComment(CommentEntity comment);
    List<CommentEntity> getRepliesByCommentId(Long commentId);
    List<CommentEntity> getCommentById(Long id);
}
