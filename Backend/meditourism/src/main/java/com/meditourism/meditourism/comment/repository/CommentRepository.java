package com.meditourism.meditourism.comment.repository;

import com.meditourism.meditourism.comment.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    Optional<List<CommentEntity>> findAllByReviewEntityId(Long reviewId);
    Optional<List<CommentEntity>> findAllByFatherCommentEntityId(Long commentId);

}
