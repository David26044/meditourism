package com.meditourism.meditourism.clinicTreatmen.service;

import com.meditourism.meditourism.clinic.dto.ClinicDTO;
import com.meditourism.meditourism.clinic.entity.ClinicEntity;
import com.meditourism.meditourism.clinicTreatmen.dto.ClinicTreatmentDTO;
import com.meditourism.meditourism.clinicTreatmen.dto.ClinicTreatmentResponseDTO;
import com.meditourism.meditourism.clinicTreatmen.entity.ClinicTreatmentEntity;
import com.meditourism.meditourism.clinicTreatmen.entity.ClinicTreatmentEntityPK;
import com.meditourism.meditourism.treatment.dto.TreatmentDTO;
import com.meditourism.meditourism.treatment.entity.TreatmentEntity;

import java.util.List;

public interface IClinicTreatmentService {

    List<ClinicDTO> getClinicsByTreatmentId(Long id);
    List<TreatmentDTO> getTreatmentsByClinicId(Long id);
    List<ClinicTreatmentResponseDTO> getAllClinicTreatments();
    ClinicTreatmentResponseDTO saveClinicTreatment(ClinicTreatmentDTO clinicTreatment);
    ClinicTreatmentResponseDTO getClinicTreatmentById(ClinicTreatmentDTO dto);
    ClinicTreatmentResponseDTO updateClinicTreatment(Long clinicId, Long treatmentId, ClinicTreatmentDTO dto);
    ClinicTreatmentResponseDTO deleteClinicTreatment(Long clinicId, Long treatmentId);
}
