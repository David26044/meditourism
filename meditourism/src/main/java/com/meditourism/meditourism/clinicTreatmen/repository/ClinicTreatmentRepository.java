package com.meditourism.meditourism.clinicTreatmen.repository;

import com.meditourism.meditourism.clinic.entity.ClinicEntity;
import com.meditourism.meditourism.clinicTreatmen.entity.ClinicTreatmentEntity;
import com.meditourism.meditourism.clinicTreatmen.entity.ClinicTreatmentEntityPK;
import com.meditourism.meditourism.treatment.entity.TreatmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClinicTreatmentRepository extends JpaRepository<ClinicTreatmentEntity, ClinicTreatmentEntityPK> {

    @Query("SELECT ct.clinic FROM ClinicTreatmentEntity ct WHERE ct.treatment.id = :treatmentId")
    List<ClinicEntity> findClinicByTreatment(@Param("treatmentId") Long treatmentId);

    @Query("SELECT ct.treatment FROM ClinicTreatmentEntity ct WHERE ct.clinic.id = :clinicId")
    List<TreatmentEntity> findTreatmentByClinic(@Param("clinicId") Long clinicId);

}
