package com.meditourism.meditourism.treatment.service;

import com.meditourism.meditourism.clinic.entity.ClinicEntity;
import com.meditourism.meditourism.exception.ResourceAlreadyExistsException;
import com.meditourism.meditourism.exception.ResourceNotFoundException;
import com.meditourism.meditourism.treatment.entity.TreatmentEntity;
import com.meditourism.meditourism.treatment.repository.TreatmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TreatmentService implements ITreatmentService{

    @Autowired
    private TreatmentRepository treatmentRepository;

    @Override
    public TreatmentEntity saveTreatment(TreatmentEntity treatment) {
        if (treatmentRepository.existsByName(treatment.getName())){
            new ResourceAlreadyExistsException("Tratamiento con nombre '" + treatment.getName() + "' ya existe");
        }
        return treatmentRepository.save(treatment);
    }

    @Override
    public TreatmentEntity updateTreatment(TreatmentEntity treatment) {

        if (!treatmentRepository.existsById(treatment.getId())){
            new ResourceNotFoundException("Tratamiento no encontrado con ID: " + treatment.getId());
        }
        return treatmentRepository.save(treatment);
    }

    @Override
    public TreatmentEntity getTreatmentById(Long id) {
        return treatmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tratamiento no encontrado con ID: " + id));
    }

    @Override
    public TreatmentEntity deleteTreatmentById(Long id) {
        TreatmentEntity treatment = treatmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tratamiento no encontrado con ID: " + id));
        treatmentRepository.delete(treatment);
        return treatment;
    }

    @Override
    public List<TreatmentEntity> getAllTreatments() {
        return treatmentRepository.findAll();
    }
}
