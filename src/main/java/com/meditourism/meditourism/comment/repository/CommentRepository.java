package com.meditourism.meditourism.comment.repository;

import com.meditourism.meditourism.comment.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
}
