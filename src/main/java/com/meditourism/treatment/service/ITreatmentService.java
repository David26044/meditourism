package com.meditourism.treatment.service;

import com.meditourism.clinic.entity.ClinicEntity;
import com.meditourism.treatment.dto.TreatmentDTO;
import com.meditourism.treatment.entity.TreatmentEntity;

import java.util.List;

public interface ITreatmentService {

    TreatmentDTO saveTreatment(TreatmentDTO dto);
    TreatmentDTO updateTreatment(Long id, TreatmentDTO dto);
    TreatmentDTO getTreatmentById(Long id);
    TreatmentDTO deleteTreatmentById(Long id);
    List<TreatmentDTO> getAllTreatments();

}
