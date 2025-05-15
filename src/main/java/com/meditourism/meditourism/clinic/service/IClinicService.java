package com.meditourism.meditourism.clinic.service;

import com.meditourism.meditourism.clinic.entity.ClinicEntity;

import java.util.List;

public interface IClinicService {

    ClinicEntity saveCLinic(ClinicEntity clinicEntity);
    ClinicEntity updateCLinic(ClinicEntity clinicEntity);
    ClinicEntity getClinicById(Long id);
    List<ClinicEntity> getAllClinics();
    ClinicEntity deleteClinicById(Long id);
}
