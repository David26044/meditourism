package com.meditourism.meditourism.clinic.repository;

import com.meditourism.meditourism.clinic.entity.ClinicEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClinicRepository extends JpaRepository<ClinicEntity, Long> {
}
