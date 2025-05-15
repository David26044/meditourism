package com.meditourism.meditourism.clinicTreatmen.service;

import com.meditourism.meditourism.clinic.entity.ClinicEntity;
import com.meditourism.meditourism.clinic.repository.ClinicRepository;
import com.meditourism.meditourism.clinicTreatmen.dto.ClinicTreatmentDTO;
import com.meditourism.meditourism.clinicTreatmen.entity.ClinicTreatmentEntity;
import com.meditourism.meditourism.clinicTreatmen.entity.ClinicTreatmentEntityPK;
import com.meditourism.meditourism.clinicTreatmen.repository.ClinicTreatmentRepository;
import com.meditourism.meditourism.exception.ResourceNotFoundException;
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
    public List<ClinicEntity> getClinicsByTreatmentId(Long id) {
        List<ClinicEntity> clinics = clinicTreatmentRepository.findClinicByTreatment(id);

        if (clinics.isEmpty()) {
            throw new ResourceNotFoundException("El tratamiento con ID " + id + " no existe o no es prestado por ninguna clínica");
        }

        return clinics;
    }


    @Override
    public List<TreatmentEntity> getTreatmentsByClinicId(Long id) {
        List<TreatmentEntity> treatments = clinicTreatmentRepository.findTreatmentByClinic(id);

        if (treatments.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron tratamientos asociados a la clínica con ID: " + id);
        }

        return treatments;
    }

    @Override
    public List<ClinicTreatmentEntity> getAllClinicTreatments() {
        return clinicTreatmentRepository.findAll();
    }

    @Override
    public ClinicTreatmentEntity saveClinicTreatment(ClinicTreatmentDTO dto) {
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
        return clinicTreatmentRepository.save(entity);
    }

    @Override
    public ClinicTreatmentEntity getClinicTreatmentById(ClinicTreatmentDTO dto) {
        //Crea llave compuesta
        ClinicTreatmentEntityPK pk = new ClinicTreatmentEntityPK();
        pk.setClinicId(dto.getClinicId());
        pk.setTreatmentId(dto.getTreatmentId());

        //La busca en el repository
        return clinicTreatmentRepository.findById(pk)
                .orElseThrow(() -> new ResourceNotFoundException("Relación no encontrada con clinic_id: " + pk.getClinicId() + " treatment_id: " + pk.getTreatmentId()));

    }
}
