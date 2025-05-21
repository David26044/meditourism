package com.meditourism.meditourism.review.repository;

import com.meditourism.meditourism.review.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
}
