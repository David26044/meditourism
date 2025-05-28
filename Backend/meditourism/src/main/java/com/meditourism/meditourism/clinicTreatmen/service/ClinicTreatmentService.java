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

@Service
public class ClinicTreatmentService implements IClinicTreatmentService {

    @Autowired
    private ClinicTreatmentRepository clinicTreatmentRepository;
    @Autowired
    private TreatmentRepository treatmentRepository;
    @Autowired
    private ClinicRepository clinicRepository;

    @Override
    public List<ClinicDTO> getClinicsByTreatmentId(Long id) {
        return ClinicDTO.fromEntityList(clinicTreatmentRepository.findClinicByTreatment(id));
    }


    @Override
    public List<TreatmentDTO> getTreatmentsByClinicId(Long id) {
        return TreatmentDTO.fromEntityList(clinicTreatmentRepository.findTreatmentByClinic(id));
    }

    @Override
    public List<ClinicTreatmentResponseDTO> getAllClinicTreatments() {
        return ClinicTreatmentResponseDTO.fromEntityList(clinicTreatmentRepository.findAll());
    }

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

    @Override
    public ClinicTreatmentResponseDTO getClinicTreatmentById(ClinicTreatmentDTO dto) {
        //Crea llave compuesta
        ClinicTreatmentEntityPK pk = new ClinicTreatmentEntityPK();
        pk.setClinicId(dto.getClinicId());
        pk.setTreatmentId(dto.getTreatmentId());

        //La busca en el repository
        return new ClinicTreatmentResponseDTO(clinicTreatmentRepository.findById(pk)
                .orElseThrow(() -> new ResourceNotFoundException("Relación no encontrada con clinic_id: " + pk.getClinicId() + " treatment_id: " + pk.getTreatmentId())));

    }

    @Override
    public ClinicTreatmentEntity getClinicTreatmentEntityById(Long clinicId, Long treatmentId) {
        ClinicTreatmentEntityPK pk = new ClinicTreatmentEntityPK();
        pk.setClinicId(clinicId);
        pk.setTreatmentId(treatmentId);
        return clinicTreatmentRepository.findById(pk)
                .orElseThrow(() -> new ResourceNotFoundException("Relación no encontrada con clinic_id: " + pk.getClinicId() + " treatment_id: " + pk.getTreatmentId()));

    }

    @Override
    public ClinicTreatmentResponseDTO updateClinicTreatment(Long clinicId, Long treatmentId, ClinicTreatmentDTO dto){
        //Crea llave compuesta
        ClinicTreatmentEntityPK pk = new ClinicTreatmentEntityPK();
        pk.setClinicId(clinicId);
        pk.setTreatmentId(treatmentId);

        //La busca en el repository
        ClinicTreatmentEntity updateClinicTreatment = clinicTreatmentRepository.findById(pk)
                .orElseThrow(() -> new ResourceNotFoundException("Relación no encontrada con clinic_id: " + pk.getClinicId() + " treatment_id: " + pk.getTreatmentId()));
       if (dto.getPrice() != null) {
           updateClinicTreatment.setPrice(dto.getPrice());
       }
        return new ClinicTreatmentResponseDTO(clinicTreatmentRepository.save(updateClinicTreatment));
    }

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
