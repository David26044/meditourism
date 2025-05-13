package com.meditourism.meditourism.clinicTreatmen.service;

import com.meditourism.meditourism.clinic.entity.ClinicEntity;
import com.meditourism.meditourism.clinicTreatmen.dto.ClinicTreatmentDTO;
import com.meditourism.meditourism.clinicTreatmen.entity.ClinicTreatmentEntity;
import com.meditourism.meditourism.clinicTreatmen.entity.ClinicTreatmentEntityPK;
import com.meditourism.meditourism.treatment.entity.TreatmentEntity;

import java.util.List;

public interface IClinicTreatmentService {

    List<ClinicEntity> getClinicByTreatmentId(Long id);
    List<TreatmentEntity> getTreatmentByClinicId(Long id);
    List<ClinicTreatmentEntity> getAllClinicTreatments();
    ClinicTreatmentEntity saveClinicTreatment(ClinicTreatmentDTO clinicTreatment);
    ClinicTreatmentEntity getClinicTreatmentById(ClinicTreatmentDTO dto);

}
