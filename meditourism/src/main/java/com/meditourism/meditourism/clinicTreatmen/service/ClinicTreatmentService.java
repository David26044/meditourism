package com.meditourism.meditourism.clinicTreatmen.service;

import com.meditourism.meditourism.clinic.dto.ClinicDTO;
import com.meditourism.meditourism.clinic.entity.ClinicEntity;
import com.meditourism.meditourism.clinic.repository.ClinicRepository;
import com.meditourism.meditourism.clinicTreatmen.dto.ClinicTreatmentDTO;
import com.meditourism.meditourism.clinicTreatmen.dto.ClinicTreatmentResponseDTO;
import com.meditourism.meditourism.clinicTreatmen.entity.ClinicTreatmentEntity;
import com.meditourism.meditourism.clinicTreatmen.entity.ClinicTreatmentEntityPK;
import com.meditourism.meditourism.clinicTreatmen.repository.ClinicTreatmentRepository;
import com.meditourism.meditourism.exception.ResourceNotFoundException;
import com.meditourism.meditourism.treatment.dto.TreatmentDTO;
import com.meditourism.meditourism.treatment.entity.TreatmentEntity;
import com.meditourism.meditourism.treatment.repository.TreatmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio para gestionar las relaciones entre clínicas y tratamientos.
 * Proporciona métodos para crear, leer, actualizar y eliminar asociaciones
 * entre clínicas y tratamientos, así como consultas relacionadas.
 */
@Service
public class ClinicTreatmentService implements IClinicTreatmentService {

    @Autowired
    private ClinicTreatmentRepository clinicTreatmentRepository;

    @Autowired
    private TreatmentRepository treatmentRepository;

    @Autowired
    private ClinicRepository clinicRepository;

    /**
     * Obtiene todas las clínicas que ofrecen un tratamiento específico.
     *
     * @param id ID del tratamiento
     * @return Lista de clínicas que ofrecen el tratamiento
     */
    @Override
    public List<ClinicDTO> getClinicsByTreatmentId(Long id) {
        return ClinicDTO.fromEntityList(clinicTreatmentRepository.findClinicByTreatment(id));
    }

    /**
     * Obtiene todos los tratamientos ofrecidos por una clínica específica.
     *
     * @param id ID de la clínica
     * @return Lista de tratamientos ofrecidos por la clínica
     */
    @Override
    public List<TreatmentDTO> getTreatmentsByClinicId(Long id) {
        return TreatmentDTO.fromEntityList(clinicTreatmentRepository.findTreatmentByClinic(id));
    }

    /**
     * Obtiene todas las relaciones entre clínicas y tratamientos.
     *
     * @return Lista de todas las relaciones clínica-tratamiento
     */
    @Override
    public List<ClinicTreatmentResponseDTO> getAllClinicTreatments() {
        return ClinicTreatmentResponseDTO.fromEntityList(clinicTreatmentRepository.findAll());
    }

    /**
     * Crea una nueva relación entre una clínica y un tratamiento.
     *
     * @param dto DTO con los datos de la relación a crear
     * @return DTO con los datos de la relación creada
     * @throws ResourceNotFoundException si no se encuentra la clínica o el tratamiento
     */
    @Override
    public ClinicTreatmentResponseDTO saveClinicTreatment(ClinicTreatmentDTO dto) {
        // Buscar clínica
        ClinicEntity clinic = clinicRepository.findById(dto.getClinicId())
                .orElseThrow(() -> new ResourceNotFoundException("Clinica no encontrada con ID: " + dto.getClinicId()));

        // Buscar tratamiento
        TreatmentEntity treatment = treatmentRepository.findById(dto.getTreatmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Tratamiento no encontrado con ID: " + dto.getTreatmentId()));

        // Crear la llave compuesta
        ClinicTreatmentEntityPK pk = new ClinicTreatmentEntityPK();
        pk.setClinicId(dto.getClinicId());
        pk.setTreatmentId(dto.getTreatmentId());

        // Crear la entidad
        ClinicTreatmentEntity entity = new ClinicTreatmentEntity();
        entity.setId(pk);
        entity.setClinic(clinic);
        entity.setTreatment(treatment);
        entity.setPrice(dto.getPrice());

        // Guardar
        return new ClinicTreatmentResponseDTO(clinicTreatmentRepository.save(entity));
    }

    /**
     * Obtiene una relación específica entre clínica y tratamiento.
     *
     * @param dto DTO con los IDs de la clínica y tratamiento
     * @return DTO con los datos de la relación encontrada
     * @throws ResourceNotFoundException si no se encuentra la relación
     */
    @Override
    public ClinicTreatmentResponseDTO getClinicTreatmentById(ClinicTreatmentDTO dto) {
        // Crea llave compuesta
        ClinicTreatmentEntityPK pk = new ClinicTreatmentEntityPK();
        pk.setClinicId(dto.getClinicId());
        pk.setTreatmentId(dto.getTreatmentId());

        // La busca en el repository
        return new ClinicTreatmentResponseDTO(clinicTreatmentRepository.findById(pk)
                .orElseThrow(() -> new ResourceNotFoundException("Relación no encontrada con clinic_id: " + pk.getClinicId() + " treatment_id: " + pk.getTreatmentId())));
    }

    /**
     * Obtiene la entidad de relación clínica-tratamiento por sus IDs.
     *
     * @param clinicId ID de la clínica
     * @param treatmentId ID del tratamiento
     * @return Entidad de la relación encontrada
     * @throws ResourceNotFoundException si no se encuentra la relación
     */
    @Override
    public ClinicTreatmentEntity getClinicTreatmentEntityById(Long clinicId, Long treatmentId) {
        ClinicTreatmentEntityPK pk = new ClinicTreatmentEntityPK();
        pk.setClinicId(clinicId);
        pk.setTreatmentId(treatmentId);
        return clinicTreatmentRepository.findById(pk)
                .orElseThrow(() -> new ResourceNotFoundException("Relación no encontrada con clinic_id: " + pk.getClinicId() + " treatment_id: " + pk.getTreatmentId()));
    }

    /**
     * Actualiza el precio de una relación clínica-tratamiento existente.
     *
     * @param clinicId ID de la clínica
     * @param treatmentId ID del tratamiento
     * @param dto DTO con el nuevo precio
     * @return DTO con los datos actualizados de la relación
     * @throws ResourceNotFoundException si no se encuentra la relación
     */
    @Override
    public ClinicTreatmentResponseDTO updateClinicTreatment(Long clinicId, Long treatmentId, ClinicTreatmentDTO dto) {
        // Crea llave compuesta
        ClinicTreatmentEntityPK pk = new ClinicTreatmentEntityPK();
        pk.setClinicId(clinicId);
        pk.setTreatmentId(treatmentId);

        // La busca en el repository
        ClinicTreatmentEntity updateClinicTreatment = clinicTreatmentRepository.findById(pk)
                .orElseThrow(() -> new ResourceNotFoundException("Relación no encontrada con clinic_id: " + pk.getClinicId() + " treatment_id: " + pk.getTreatmentId()));

        if (dto.getPrice() != null) {
            updateClinicTreatment.setPrice(dto.getPrice());
        }

        return new ClinicTreatmentResponseDTO(clinicTreatmentRepository.save(updateClinicTreatment));
    }

    /**
     * Elimina una relación clínica-tratamiento existente.
     *
     * @param clinicId ID de la clínica
     * @param treatmentId ID del tratamiento
     * @return DTO con los datos de la relación eliminada
     * @throws ResourceNotFoundException si no se encuentra la relación
     */
    @Override
    public ClinicTreatmentResponseDTO deleteClinicTreatment(Long clinicId, Long treatmentId) {
        // Crear la llave compuesta
        ClinicTreatmentEntityPK pk = new ClinicTreatmentEntityPK();
        pk.setClinicId(clinicId);
        pk.setTreatmentId(treatmentId);

        // Buscar la relación en la base de datos
        ClinicTreatmentEntity entity = clinicTreatmentRepository.findById(pk)
                .orElseThrow(() -> new ResourceNotFoundException("Relación no encontrada con clinic_id: " + clinicId + " treatment_id: " + treatmentId));

        // Eliminar la entidad
        clinicTreatmentRepository.delete(entity);

        // Devolver los datos de la relación eliminada
        return new ClinicTreatmentResponseDTO(entity);
    }
}