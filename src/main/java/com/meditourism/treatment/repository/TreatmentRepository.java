package com.meditourism.treatment.repository;

import com.meditourism.treatment.entity.TreatmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TreatmentRepository extends JpaRepository<TreatmentEntity, Long> {

    boolean existsByName(String name);
}
