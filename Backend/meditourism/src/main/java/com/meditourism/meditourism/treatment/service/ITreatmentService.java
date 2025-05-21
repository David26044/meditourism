package com.meditourism.meditourism.treatment.service;

import com.meditourism.meditourism.clinic.entity.ClinicEntity;
import com.meditourism.meditourism.treatment.entity.TreatmentEntity;

import java.util.List;

public interface ITreatmentService {

    TreatmentEntity saveTreatment(TreatmentEntity treatment);
    TreatmentEntity updateTreatment(TreatmentEntity treatment);
    TreatmentEntity getTreatmentById(Long id);
    TreatmentEntity deleteTreatmentById(Long id);
    List<TreatmentEntity> getAllTreatments();

}
