package com.meditourism.meditourism.review.repository;

import com.meditourism.meditourism.review.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    Optional<List<ReviewEntity>> findAllByClinicId(Long id);

}
