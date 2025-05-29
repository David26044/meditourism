package com.meditourism.meditourism.treatment.service;

import com.meditourism.meditourism.clinic.entity.ClinicEntity;
import com.meditourism.meditourism.clinicTreatmen.service.ClinicTreatmentService;
import com.meditourism.meditourism.exception.ResourceAlreadyExistsException;
import com.meditourism.meditourism.exception.ResourceNotFoundException;
import com.meditourism.meditourism.treatment.dto.TreatmentDTO;
import com.meditourism.meditourism.treatment.entity.TreatmentEntity;
import com.meditourism.meditourism.treatment.repository.TreatmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio para gestionar operaciones relacionadas con tratamientos médicos
 */
@Service
public class TreatmentService implements ITreatmentService {

    @Autowired
    private TreatmentRepository treatmentRepository;

    /**
     * Guarda un nuevo tratamiento en la base de datos
     * @param dto Objeto DTO con los datos del tratamiento
     * @return TreatmentDTO con los datos del tratamiento guardado
     * @throws ResourceAlreadyExistsException si ya existe un tratamiento con el mismo nombre
     */
    @Override
    public TreatmentDTO saveTreatment(TreatmentDTO dto) {
        if (treatmentRepository.existsByName(dto.getName())){
            new ResourceAlreadyExistsException("Tratamiento con nombre '" + dto.getName() + "' ya existe");
        }

        TreatmentEntity treatment = new TreatmentEntity();

        treatment.setDescription(dto.getDescription());
        treatment.setName(dto.getName());

        return new TreatmentDTO(treatmentRepository.save(treatment));
    }

    /**
     * Actualiza un tratamiento existente
     * @param id ID del tratamiento a actualizar
     * @param dto Objeto DTO con los nuevos datos del tratamiento
     * @return TreatmentDTO con los datos del tratamiento actualizado
     * @throws ResourceNotFoundException si no se encuentra el tratamiento
     */
    @Override
    public TreatmentDTO updateTreatment(Long id, TreatmentDTO dto) {
        TreatmentEntity treatment = treatmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tratamiento no encontrado con ID: " + dto.getId()));

        if (dto.getDescription() != null){
            treatment.setDescription(dto.getDescription());
        }
        if (dto.getName() != null){
            treatment.setName(dto.getName());
        }
        return new TreatmentDTO(treatmentRepository.save(treatment));
    }

    /**
     * Obtiene un tratamiento por su ID
     * @param id ID del tratamiento a buscar
     * @return TreatmentDTO con los datos del tratamiento encontrado
     * @throws ResourceNotFoundException si no se encuentra el tratamiento
     */
    @Override
    public TreatmentDTO getTreatmentById(Long id) {
        return new TreatmentDTO(treatmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tratamiento no encontrado con ID: " + id)));
    }

    /**
     * Elimina un tratamiento por su ID
     * @param id ID del tratamiento a eliminar
     * @return TreatmentDTO con los datos del tratamiento eliminado
     * @throws ResourceNotFoundException si no se encuentra el tratamiento
     */
    @Override
    public TreatmentDTO deleteTreatmentById(Long id) {
        TreatmentEntity treatment = treatmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tratamiento no encontrado con ID: " + id));
        treatmentRepository.delete(treatment);
        return new TreatmentDTO(treatment);
    }

    /**
     * Obtiene todos los tratamientos disponibles
     * @return Lista de TreatmentDTO con todos los tratamientos
     */
    @Override
    public List<TreatmentDTO> getAllTreatments() {
        List<TreatmentEntity> treatments = treatmentRepository.findAll();
        return TreatmentDTO.fromEntityList(treatments);
    }
}
