package com.meditourism.meditourism.clinic.service;

import com.meditourism.meditourism.clinic.dto.ClinicDTO;
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
    public ClinicDTO saveClinic(ClinicDTO dto) {
        ClinicEntity clinicEntity = new ClinicEntity();
        clinicEntity.setDescription(dto.getDescription());
        clinicEntity.setName(dto.getName());
        clinicEntity.setContactInfo(dto.getContactInfo());
        clinicEntity.setAddress(dto.getAddress());
        return new ClinicDTO(clinicRepository.save(clinicEntity));
    }


    @Override
    public ClinicDTO updateClinic(Long id, ClinicDTO dto) {
        ClinicEntity updateClinic = clinicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clínica no encontrada con ID: " + id));
        if(dto.getName() != null){
            updateClinic.setName(dto.getName());
        }
        if (dto.getDescription() != null){
            updateClinic.setDescription(dto.getDescription());
        }
        if (dto.getContactInfo() != null){
            updateClinic.setContactInfo(dto.getContactInfo());
        }
        if(dto.getAddress() != null){
            updateClinic.setAddress(dto.getAddress());
        }
        return new ClinicDTO(clinicRepository.save(updateClinic));
    }

    @Override
    public ClinicDTO getClinicById(Long id) {
        return new ClinicDTO(clinicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clínica no encontrada con ID: " + id)));
    }

    @Override
    public List<ClinicDTO> getAllClinics() {
        return ClinicDTO.fromEntityList(clinicRepository.findAll());
    }

    @Override
    public ClinicDTO deleteClinicById(Long id) {
        ClinicEntity clinic = clinicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clínica no encontrada con ID: " + id));
        clinicRepository.delete(clinic);
        return new ClinicDTO(clinic);
    }

    @Override
    public ClinicEntity getClinicEntityById(Long id){
        return clinicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clínica no encontrada con ID: " + id));
    }
}

