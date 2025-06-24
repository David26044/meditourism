package com.meditourism.meditourism.review.repository;

import com.meditourism.meditourism.review.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    Optional<List<ReviewEntity>> findAllByClinicId(Long id);

    @Query("SELECT r FROM ReviewEntity r WHERE r.id IN (SELECT MAX(r1.id) FROM ReviewEntity r1 GROUP BY r1.id ORDER BY r1.id DESC LIMIT 3)")
    List<ReviewEntity> findTop3ByIdOrderByDesc();

    List<ReviewEntity> findAllByUserId(Long id);
}
