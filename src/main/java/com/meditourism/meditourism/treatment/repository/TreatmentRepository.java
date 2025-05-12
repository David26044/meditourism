package com.meditourism.meditourism.treatment.repository;

import com.meditourism.meditourism.treatment.entity.TreatmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TreatmentRepository extends JpaRepository<TreatmentEntity, Long> {
}
