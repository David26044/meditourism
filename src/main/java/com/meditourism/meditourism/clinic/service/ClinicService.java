package com.meditourism.meditourism.clinic.service;

import com.meditourism.meditourism.clinic.entity.ClinicEntity;
import com.meditourism.meditourism.clinic.repository.ClinicRepository;
import com.meditourism.meditourism.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClinicService implements IClinicService {

    @Autowired
    private ClinicRepository clinicRepository;

    @Override
    public ClinicEntity saveCLinic(ClinicEntity clinicEntity) {
        return clinicRepository.save(clinicEntity);
    }

    @Override
    public ClinicEntity updateCLinic(ClinicEntity clinicEntity) {
        if (!clinicRepository.existsById(clinicEntity.getId())) {
            throw new ResourceNotFoundException("Clínica no encontrada con ID: " + clinicEntity.getId());
        }
        return clinicRepository.save(clinicEntity);
    }

    @Override
    public ClinicEntity getClinicById(Long id) {
        return clinicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clínica no encontrada con ID: " + id));
    }

    @Override
    public List<ClinicEntity> getAllClinics() {
        return clinicRepository.findAll();
    }

    @Override
    public ClinicEntity deleteClinicById(Long id) {
        ClinicEntity clinic = clinicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clínica no encontrada con ID: " + id));
        clinicRepository.delete(clinic);
        return clinic;
    }
}

