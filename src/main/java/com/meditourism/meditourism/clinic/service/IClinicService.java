package com.meditourism.meditourism.clinic.service;

import com.meditourism.meditourism.clinic.entity.ClinicEntity;

public interface IClinicService {

    ClinicEntity saveCLinic(ClinicEntity clinicEntity);
    ClinicEntity updateCLinic(ClinicEntity clinicEntity);
    ClinicEntity getClinicById(Long id);
    ClinicEntity deleteClinicById(Long id);
}
