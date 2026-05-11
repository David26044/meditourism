package com.meditourism.clinicTreatmen.service;

import com.meditourism.clinic.dto.ClinicDTO;
import com.meditourism.clinic.entity.ClinicEntity;
import com.meditourism.clinicTreatmen.dto.ClinicTreatmentDTO;
import com.meditourism.clinicTreatmen.dto.ClinicTreatmentResponseDTO;
import com.meditourism.clinicTreatmen.entity.ClinicTreatmentEntity;
import com.meditourism.clinicTreatmen.entity.ClinicTreatmentEntityPK;
import com.meditourism.treatment.dto.TreatmentDTO;
import com.meditourism.treatment.entity.TreatmentEntity;

import java.util.List;

public interface IClinicTreatmentService {

    List<ClinicDTO> getClinicsByTreatmentId(Long id);
    List<TreatmentDTO> getTreatmentsByClinicId(Long id);
    List<ClinicTreatmentResponseDTO> getAllClinicTreatments();
    ClinicTreatmentResponseDTO saveClinicTreatment(ClinicTreatmentDTO clinicTreatment);
    ClinicTreatmentResponseDTO getClinicTreatmentById(ClinicTreatmentDTO dto);

    ClinicTreatmentEntity getClinicTreatmentEntityById(Long clinicId, Long treatmentId);

    ClinicTreatmentResponseDTO updateClinicTreatment(Long clinicId, Long treatmentId, ClinicTreatmentDTO dto);
    ClinicTreatmentResponseDTO deleteClinicTreatment(Long clinicId, Long treatmentId);
}
