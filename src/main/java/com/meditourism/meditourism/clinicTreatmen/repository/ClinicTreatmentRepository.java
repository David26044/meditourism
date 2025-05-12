package com.meditourism.meditourism.clinicTreatmen.repository;

import com.meditourism.meditourism.clinicTreatmen.entity.ClinicTreatmentEntity;
import com.meditourism.meditourism.clinicTreatmen.entity.ClinicTreatmentEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClinicTreatmentRepository extends JpaRepository<ClinicTreatmentEntity, ClinicTreatmentEntityPK> {
}
