package com.meditourism.clinic.service;

import com.meditourism.clinic.dto.ClinicDTO;
import com.meditourism.clinic.entity.ClinicEntity;

import java.util.List;

public interface IClinicService {

    ClinicDTO saveClinic(ClinicDTO dto);

    ClinicDTO updateClinic(Long id, ClinicDTO dto);

    ClinicDTO getClinicById(Long id);
    List<ClinicDTO> getAllClinics();
    ClinicDTO deleteClinicById(Long id);

    ClinicEntity getClinicEntityById(Long id);
}
