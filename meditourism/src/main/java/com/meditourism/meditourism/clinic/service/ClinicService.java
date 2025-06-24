package com.meditourism.meditourism.clinic.service;

import com.meditourism.meditourism.clinic.dto.ClinicDTO;
import com.meditourism.meditourism.clinic.entity.ClinicEntity;
import com.meditourism.meditourism.clinic.repository.ClinicRepository;
import com.meditourism.meditourism.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio para manejar las operaciones relacionadas con clínicas.
 */
@Service
public class ClinicService implements IClinicService {

    @Autowired
    private ClinicRepository clinicRepository;

    /**
     * Guarda una nueva clínica en la base de datos.
     *
     * @param dto DTO con los datos de la clínica a guardar
     * @return DTO con los datos de la clínica guardada
     */
    @Override
    public ClinicDTO saveClinic(ClinicDTO dto) {
        ClinicEntity clinicEntity = new ClinicEntity();
        clinicEntity.setDescription(dto.getDescription());
        clinicEntity.setName(dto.getName());
        clinicEntity.setContactInfo(dto.getContactInfo());
        clinicEntity.setAddress(dto.getAddress());
        return new ClinicDTO(clinicRepository.save(clinicEntity));
    }

    /**
     * Actualiza los datos de una clínica existente.
     *
     * @param id ID de la clínica a actualizar
     * @param dto DTO con los nuevos datos de la clínica
     * @return DTO con los datos actualizados de la clínica
     * @throws ResourceNotFoundException Si no se encuentra la clínica con el ID especificado
     */
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

    /**
     * Obtiene una clínica por su ID.
     *
     * @param id ID de la clínica a buscar
     * @return DTO con los datos de la clínica encontrada
     * @throws ResourceNotFoundException Si no se encuentra la clínica con el ID especificado
     */
    @Override
    public ClinicDTO getClinicById(Long id) {
        return new ClinicDTO(clinicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clínica no encontrada con ID: " + id)));
    }

    /**
     * Obtiene todas las clínicas registradas.
     *
     * @return Lista de DTOs con todas las clínicas
     */
    @Override
    public List<ClinicDTO> getAllClinics() {
        return ClinicDTO.fromEntityList(clinicRepository.findAll());
    }

    /**
     * Elimina una clínica por su ID.
     *
     * @param id ID de la clínica a eliminar
     * @return DTO con los datos de la clínica eliminada
     * @throws ResourceNotFoundException Si no se encuentra la clínica con el ID especificado
     */
    @Override
    public ClinicDTO deleteClinicById(Long id) {
        ClinicEntity clinic = clinicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clínica no encontrada con ID: " + id));
        clinicRepository.delete(clinic);
        return new ClinicDTO(clinic);
    }

    /**
     * Obtiene la entidad de una clínica por su ID.
     *
     * @param id ID de la clínica a buscar
     * @return Entidad de la clínica encontrada
     * @throws ResourceNotFoundException Si no se encuentra la clínica con el ID especificado
     */
    @Override
    public ClinicEntity getClinicEntityById(Long id){
        return clinicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clínica no encontrada con ID: " + id));
    }
}