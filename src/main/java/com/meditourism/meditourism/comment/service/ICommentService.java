package com.meditourism.meditourism.comment.service;

import com.meditourism.meditourism.comment.entity.CommentEntity;

import java.util.List;

public interface ICommentService {
    List<CommentEntity> getCommentsByReviewId(Long reviewId);
    void saveComment(CommentEntity comment);
    void deleteComment(Long id);
    void updateComment(CommentEntity comment);
    List<CommentEntity> getRepliesByCommentId(Long commentId);
    List<CommentEntity> getCommentById(Long id);
}
